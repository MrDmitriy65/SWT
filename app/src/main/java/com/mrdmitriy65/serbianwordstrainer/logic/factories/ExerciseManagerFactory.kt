package com.mrdmitriy65.serbianwordstrainer.logic.factories

import com.mrdmitriy65.serbianwordstrainer.data.WordPairDao
import com.mrdmitriy65.serbianwordstrainer.logic.FloatLearnExerciseManager
import com.mrdmitriy65.serbianwordstrainer.logic.IExerciseManager
import com.mrdmitriy65.serbianwordstrainer.logic.LearnStartedWordsExerciseGenerator
import com.mrdmitriy65.serbianwordstrainer.logic.StandartResultTrainingProcessor

class ExerciseManagerFactory {
    fun CreateExerciseManager(dao: WordPairDao): IExerciseManager{
        val generator = LearnStartedWordsExerciseGenerator(dao)
        val resultProcessor = StandartResultTrainingProcessor(dao)
        return FloatLearnExerciseManager(generator, resultProcessor)
    }
}