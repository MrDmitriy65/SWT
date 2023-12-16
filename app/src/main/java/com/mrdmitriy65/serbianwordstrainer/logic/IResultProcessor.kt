package com.mrdmitriy65.serbianwordstrainer.logic

import com.mrdmitriy65.serbianwordstrainer.data.entities.WordPair
import com.mrdmitriy65.serbianwordstrainer.models.Exercise

interface IResultProcessor {
    fun processExercises(results: List<Exercise>): List<WordPair>
}