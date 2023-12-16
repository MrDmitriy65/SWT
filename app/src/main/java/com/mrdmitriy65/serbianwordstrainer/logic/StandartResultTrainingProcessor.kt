package com.mrdmitriy65.serbianwordstrainer.logic

import com.mrdmitriy65.serbianwordstrainer.constants.Constants
import com.mrdmitriy65.serbianwordstrainer.data.WordPairDao
import com.mrdmitriy65.serbianwordstrainer.models.Exercise
import com.mrdmitriy65.serbianwordstrainer.models.ResultTrainigModel
import com.mrdmitriy65.serbianwordstrainer.models.TranslationType
import kotlinx.coroutines.runBlocking

class StandartResultTrainingProcessor(private val dao: WordPairDao) : IResultProcessor {

    override fun processExercises(results: List<Exercise>): Unit {
        val pairs = results.map {
            if (it.pair.translationType == TranslationType.DIRECT) {
                ResultTrainigModel(it.pair.question, it.pair.answer, it.isCorrect)
            } else {
                ResultTrainigModel(it.pair.answer, it.pair.question, it.isCorrect)
            }
        }.groupBy { "${it.russian}_${it.serbian}" }

        runBlocking {
            for (word in pairs) {
                val rtm = word.value.first()
                val toChange = dao.getWordPairByWords(rtm.russian, rtm.serbian)
                if (word.value.all { it.isCorrect }) {
                    if (toChange.learnLevel < Constants.WORD_LAST_LEARN_LEVEL) {
                        dao.updateLevel(toChange.id, toChange.learnLevel + 1)
                    }
                } else if (word.value.all { !it.isCorrect }) {
                    if (toChange.learnLevel > Constants.WORD_START_LEARN_LEVEL) {
                        dao.updateLevel(toChange.id, toChange.learnLevel - 1)
                    }
                }
            }
        }
    }
}