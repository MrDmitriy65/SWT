package com.mrdmitriy65.serbianwordstrainer.models

class ExercisePair(
    private val questionWord: String,
    private val answerWord: String,
    val translationType: TranslationType,
    val pronounce: String = ""
) {

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