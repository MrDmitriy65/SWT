package com.mrdmitriy65.serbianwordstrainer.data

import androidx.room.*
import com.mrdmitriy65.serbianwordstrainer.data.entities.Category
import com.mrdmitriy65.serbianwordstrainer.data.entities.WordPair
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

    // Categories
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(catgory: Category)

    @Delete
    suspend fun delete(catgory: Category)

    @Update
    suspend fun update(catgory: Category)

    @Query("SELECT * FROM categories WHERE id = :id")
    fun getCategoryById(id: Int): Flow<Category>

    @Query("SELECT EXISTS (SELECT * FROM categories WHERE name = :name LIMIT 1)")
    suspend fun isCategoryExists(name: String): Boolean

    @Query("SELECT * FROM categories")
    fun getAllCategories(): Flow<List<Category>>
}