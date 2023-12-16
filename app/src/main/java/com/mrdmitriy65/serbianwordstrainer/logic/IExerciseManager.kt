package com.mrdmitriy65.serbianwordstrainer.logic

import com.mrdmitriy65.serbianwordstrainer.models.Exercise
import com.mrdmitriy65.serbianwordstrainer.models.TranslatePair
import java.io.Serializable

interface IExerciseManager : Serializable {

    fun getCurrentExercise(): Exercise
    fun setAnswer(userAnswer: String?)
    fun setNextExercise()
    fun isTrainingComplete(): Boolean
    fun getPossibleAnswers(exercise: Exercise): List<String>
    fun getResults(): List<Exercise>

    fun completeTraining()
}