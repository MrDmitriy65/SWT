package com.mrdmitriy65.serbianwordstrainer.logic.factories

import com.mrdmitriy65.serbianwordstrainer.data.WordPairDao
import com.mrdmitriy65.serbianwordstrainer.logic.FloatLearnExerciseManager
import com.mrdmitriy65.serbianwordstrainer.logic.IExerciseManager
import com.mrdmitriy65.serbianwordstrainer.logic.LearnStartedWordsExerciseGenerator

class ExerciseManagerFactory {
    fun CreateExerciseManager(dao: WordPairDao): IExerciseManager{
        val generator = LearnStartedWordsExerciseGenerator(dao)
        return FloatLearnExerciseManager(generator)
    }
}