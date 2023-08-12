package com.mrdmitriy65.serbianwordstrainer.models

import java.io.Serializable

data class TranslatePair(val word: String, val translate: String, val categoryId: Int) : Serializable {

    constructor(word: String, translate: String) : this(word, translate, 0)
    {
    }
}