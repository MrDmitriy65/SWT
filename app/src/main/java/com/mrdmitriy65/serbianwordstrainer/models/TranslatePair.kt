package com.mrdmitriy65.serbianwordstrainer.models

data class TranslatePair(
    val word: String,
    val translate: String,
    val categoryId: Int,
    val pronunciation: String
) {

    constructor(word: String, translate: String) : this(word, translate, 0, "") {
    }

    constructor(word: String, translate: String, pronunciation: String) : this(
        word,
        translate,
        0,
        pronunciation
    ) {
    }
}