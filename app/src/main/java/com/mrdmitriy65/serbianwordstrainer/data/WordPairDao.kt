package com.mrdmitriy65.serbianwordstrainer.data

import androidx.room.*
import com.mrdmitriy65.serbianwordstrainer.data.entities.Category
import com.mrdmitriy65.serbianwordstrainer.data.entities.WordPair
import com.mrdmitriy65.serbianwordstrainer.data.relations.CategoryWithWordPair
import kotlinx.coroutines.flow.Flow

@Dao
interface WordPairDao {
    // Word pairs
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(wordPair: WordPair)

    @Delete
    suspend fun delete(wordPair: WordPair)

    @Update
    suspend fun update(wordPair: WordPair)

    @Query("UPDATE word_pairs SET learn_level = :newWordLevel WHERE id = :id")
    suspend fun updateLevel(id:Int, newWordLevel: Int)

    @Query("SELECT * FROM word_pairs WHERE id = :id")
    fun getWordPairById(id: Int): Flow<WordPair>

    @Query("SELECT * FROM word_pairs WHERE russian = :russian AND serbian = :serbian LIMIT 1")
    suspend fun getWordPairByWords(russian: String, serbian: String): WordPair

    @Query("SELECT EXISTS (SELECT * FROM word_pairs WHERE russian = :russian AND serbian = :serbian LIMIT 1)")
    suspend fun isWordPairExists(russian:String, serbian: String): Boolean

    @Query("SELECT * FROM word_pairs")
    fun getAllWordPairs(): Flow<List<WordPair>>

    @Query("SELECT * FROM word_pairs WHERE category_id = :categoryId")
    fun getWordPairsByCategoryId(categoryId: Int): Flow<List<WordPair>>

    @Query("SELECT * FROM word_pairs WHERE learn_level >= :startLevel AND learn_level <= :endLevel")
    suspend fun getWordPairsBetweenLevels(startLevel: Int, endLevel: Int): List<WordPair>

    @Query("SELECT * FROM word_pairs WHERE learn_level = 0 LIMIT :count")
    suspend fun getWordPairsNotStarted(count: Int): List<WordPair>

    @Query("SELECT * FROM word_pairs WHERE russian not in (:words) AND serbian not in (:words) ORDER BY RANDOM() LIMIT :takeCount")
    suspend fun getRandomWordsNotInRange(words: List<String>, takeCount:Int): List<WordPair>

    @Query("DELETE FROM word_pairs WHERE russian = :russian AND serbian = :serbian")
    suspend fun deletePair(russian: String, serbian: String)

    @Query("DELETE FROM word_pairs WHERE category_id = :categoryId")
    suspend fun deletePairInCategory(categoryId: Int)

    // Categories
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(catgory: Category)

    @Delete
    suspend fun delete(category: Category)

    @Update
    suspend fun update(catgory: Category)

    @Query("SELECT * FROM categories WHERE id = :id")
    fun getCategoryById(id: Int): Flow<Category>

    @Query("SELECT * FROM categories WHERE name = :name")
    suspend fun getCategoryByName(name: String): Category

    @Query("SELECT EXISTS (SELECT * FROM categories WHERE name = :name LIMIT 1)")
    suspend fun isCategoryExists(name: String): Boolean

    @Query("SELECT * FROM categories")
    fun getAllCategories(): Flow<List<Category>>

    // For Backup
    @Query("SELECT * FROM categories")
    suspend fun getAllCategoriesWithWords(): List<CategoryWithWordPair>
}