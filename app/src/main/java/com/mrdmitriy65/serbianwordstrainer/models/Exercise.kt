package com.mrdmitriy65.serbianwordstrainer.models

import java.io.Serializable

class Exercise(
    val pair: ExercisePair,
    val exerciseType: ExerciseType,
    val isSpeakable: Boolean = false
) : Serializable {

    var isCompleted: Boolean = false
    get() = field
    set(value) {field = if (field) true else value }

    var isCorrect: Boolean = false
}