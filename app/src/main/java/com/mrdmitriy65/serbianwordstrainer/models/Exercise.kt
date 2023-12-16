package com.mrdmitriy65.serbianwordstrainer.models

class Exercise(
    val pair: ExercisePair,
    val exerciseType: ExerciseType,
    val isSpeakable: Boolean = false
) {

    var isCompleted: Boolean = false
        get() = field
        set(value) {
            field = if (field) true else value
        }

    var isCorrect: Boolean = false

    override fun equals(other: Any?): Boolean {
        return other is Exercise &&
                pair.question == other.pair.question &&
                pair.answer == other.pair.answer &&
                pair.translationType == other.pair.translationType &&
                exerciseType == other.exerciseType &&
                isSpeakable == other.isSpeakable
    }
}