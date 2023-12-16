package com.mrdmitriy65.serbianwordstrainer.logic

import com.mrdmitriy65.serbianwordstrainer.data.entities.WordPair
import com.mrdmitriy65.serbianwordstrainer.models.Exercise

interface IExerciseGenerator {
    suspend fun generateExercises(): List<Exercise>
    suspend fun generateWrongAnswers(wordsToExclude: List<String>, takeCount:Int): List<WordPair>
}