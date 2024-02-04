package com.mrdmitriy65.serbianwordstrainer.models

class ExercisePair(
    private val questionWord: String,
    private val answerWord: String,
    val translationType: TranslationType,
    val pronounce: String = ""
) {

    val question: String
        get() = when (translationType) {
            TranslationType.DIRECT -> questionWord
            else -> answerWord
        }

    val answer: String
        get() = when (translationType) {
            TranslationType.DIRECT -> answerWord
            else -> questionWord
        }

    override fun equals(other: Any?): Boolean {
        return hashCode() == other.hashCode() &&
                other is ExercisePair &&
                questionWord == other.questionWord &&
                answerWord == other.answerWord
    }

    override fun hashCode(): Int {
        var result = questionWord.hashCode()
        result = 31 * result + answerWord.hashCode()
        result = 31 * result + translationType.hashCode()
        result = 31 * result + pronounce.hashCode()
        return result
    }
}