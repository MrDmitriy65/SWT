package com.mrdmitriy65.serbianwordstrainer.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "word_pairs",
    foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = ["id"],
            childColumns = ["category_id"],
            onDelete = ForeignKey.NO_ACTION
        )]
)
data class WordPair(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "russian")
    val russian: String,
    @ColumnInfo(name = "serbian")
    val serbian: String,
    @ColumnInfo(name = "category_id")
    val categoryId: Int,
    @ColumnInfo(name = "comment")
    val comment: String,
    @ColumnInfo(name = "pronunciation", defaultValue = "")
    val pronunciation: String
)