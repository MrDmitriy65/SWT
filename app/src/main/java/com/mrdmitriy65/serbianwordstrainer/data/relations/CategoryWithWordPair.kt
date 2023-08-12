package com.mrdmitriy65.serbianwordstrainer.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.mrdmitriy65.serbianwordstrainer.data.entities.Category
import com.mrdmitriy65.serbianwordstrainer.data.entities.WordPair

data class CategoryWithWordPair(
    @Embedded val category: Category,
    @Relation(
        parentColumn = "id",
        entityColumn = "category_id"
    )
    val wordPairs: List<WordPair>
)