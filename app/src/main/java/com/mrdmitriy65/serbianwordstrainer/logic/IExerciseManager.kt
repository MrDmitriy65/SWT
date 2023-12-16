package com.mrdmitriy65.serbianwordstrainer.logic

import com.mrdmitriy65.serbianwordstrainer.models.Exercise
import com.mrdmitriy65.serbianwordstrainer.models.TranslatePair
import java.io.Serializable

interface IExerciseManager : Serializable {
    val wordToLearn: List<TranslatePair>

    fun getCurrentExercise(): Exercise
    fun configureExercises()
    fun setAnswer(userAnswer: String?)
    fun setNextExercise()
    fun isTrainingComplete(): Boolean
    fun getPosibleAnswers(exercise: Exercise): List<String>
    fun getResults(): List<Exercise>

    fun setWords(wordDictionary: List<TranslatePair>, wordsToLearnCount: Int)
    fun resetWords()

    companion object {
        const val EXERCISE_MANAGER_KEY = "exercise_manager_key"
    }
}