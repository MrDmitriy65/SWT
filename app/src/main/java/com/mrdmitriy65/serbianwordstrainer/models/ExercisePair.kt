package com.mrdmitriy65.serbianwordstrainer.models

import java.io.Serializable

class ExercisePair(
    private val questionWord: String,
    private val answerWord: String,
    val translationType: TranslationType
) : Serializable {

    val question: String
    get() = when(translationType){
        TranslationType.DIRECT -> questionWord
        else -> answerWord
    }

    val answer: String
    get() = when(translationType) {
        TranslationType.DIRECT -> answerWord
        else -> questionWord
    }
}