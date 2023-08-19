package com.mrdmitriy65.serbianwordstrainer.models

data class TranslatePair(val word: String, val translate: String, val categoryId: Int) {

    constructor(word: String, translate: String) : this(word, translate, 0)
    {
    }
}