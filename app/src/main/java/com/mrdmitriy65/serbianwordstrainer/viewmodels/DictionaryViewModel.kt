package com.mrdmitriy65.serbianwordstrainer.viewmodels

import androidx.lifecycle.*
import com.mrdmitriy65.serbianwordstrainer.data.WordPairDao
import com.mrdmitriy65.serbianwordstrainer.data.entities.Category
import com.mrdmitriy65.serbianwordstrainer.data.entities.WordPair
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class DictionaryViewModel(
    private val wordPairDao: WordPairDao
) : ViewModel() {

    var allCategories: LiveData<List<Category>> = wordPairDao.getAllCategories().asLiveData()
    var allWords: LiveData<List<WordPair>> = wordPairDao.getAllWordPairs().asLiveData()

    fun getWordsForCategory(categoryId: Int): LiveData<List<WordPair>> {
        return wordPairDao.getWordPairsByCategoryId(categoryId).asLiveData()
    }

    fun addNewCategory(name: String) {
        viewModelScope.launch {
            if (!wordPairDao.isCategoryExists(name)) {
                wordPairDao.insert(Category(name = name))
            }
        }
    }

    fun addNewPair(russian: String, serbian: String, categoryId: Int, comment: String = "") {
        viewModelScope.launch {
            if (!wordPairDao.isWordPairExists(russian, serbian)) {
                wordPairDao.insert(
                    WordPair(
                        russian = russian,
                        serbian = serbian,
                        categoryId = categoryId,
                        comment = comment
                    )
                )
            }
        }
    }

    fun isPairValid(russianWord: String, serbianWord: String, categoryId: Int): Boolean {
        if(russianWord.isBlank() || serbianWord.isBlank() || categoryId < 1){
            return false
        }
        return true
    }

    fun isPairValidForChange(id: Int, russian: String, serbian: String): Boolean{
        var existingWord: WordPair?
        runBlocking {
            existingWord = wordPairDao.getWordPairByWords(russian, serbian)
        }

        if(existingWord == null)
        {
            return true
        }
        if(existingWord?.id == id){
            return true
        }
        return false
    }

    fun getPairById(id:Int):LiveData<WordPair> = wordPairDao.getWordPairById(id).asLiveData()

    fun updatePair(id: Int, russian: String, serbian: String, comment: String, categoryId: Int){
        viewModelScope.launch {
            val wordPair = WordPair(id, russian, serbian, categoryId, comment)
            wordPairDao.update(wordPair)
        }
    }
}

class DictionaryViewModelFactory(
    private val dao: WordPairDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DictionaryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DictionaryViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}