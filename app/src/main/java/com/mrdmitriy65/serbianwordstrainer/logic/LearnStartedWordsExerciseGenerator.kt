package com.mrdmitriy65.serbianwordstrainer.logic

import com.mrdmitriy65.serbianwordstrainer.constants.Constants
import com.mrdmitriy65.serbianwordstrainer.data.WordPairDao
import com.mrdmitriy65.serbianwordstrainer.data.entities.WordPair
import com.mrdmitriy65.serbianwordstrainer.models.Exercise
import com.mrdmitriy65.serbianwordstrainer.models.ExercisePair
import com.mrdmitriy65.serbianwordstrainer.models.ExerciseType
import com.mrdmitriy65.serbianwordstrainer.models.TranslationType

class LearnStartedWordsExerciseGenerator(private val dao: WordPairDao) : IExerciseGenerator {

    suspend fun generateExercises(): List<Exercise> {
        val words = getWords()


        val result = mutableListOf<Exercise>()

        for (word in words){
            val pair = ExercisePair(word.russian.trim(), word.serbian.trim(), TranslationType.DIRECT)
            val pairReverse = ExercisePair(word.russian.trim(), word.serbian.trim(), TranslationType.DIRECT, word.serbian)
            val type = GetExerciseType(word)

            result.add(Exercise(pair, type, false))
            result.add(Exercise(pairReverse, type, true))
        }

        return result.toList()
    }

    private suspend fun getWords(): List<WordPair> {
        val words = dao.getWordPairsBetweenLevels(
            Constants.WORD_START_LEARN_LEVEL,
            Constants.WORD_LAST_LEARN_LEVEL
        )

        if (words.count() < Constants.EXERCISE_BUFFER_WORDS_COUNT) {
            val buffPotions =
                Constants.EXERCISE_BUFFER_WORDS_COUNT / Constants.EXERCISE_PORTION_ADD_TO_BUFFER_WORDS_COUNT
            val currentCountInPortions =
                words.count() / Constants.EXERCISE_PORTION_ADD_TO_BUFFER_WORDS_COUNT
            val difference = buffPotions - currentCountInPortions
            val toAddCount = difference * Constants.EXERCISE_PORTION_ADD_TO_BUFFER_WORDS_COUNT

            return words + dao.getWordPairsNotStarted(toAddCount)
        }
        return words
    }

    private fun GetExerciseType (word: WordPair): ExerciseType {
        val level = word.learnLevel / Constants.WORD_ANSWERS_TO_INCREASE_EXERCISE_LEVEL
        return when(level) {
            0 -> ExerciseType.CHOSE_FROM_VARIANTS
            1 -> ExerciseType.WRITE_WORD_FROM_CHARACTERS
            else -> ExerciseType.WRITE_ANSWER
        }
    }
}