package com.mrdmitriy65.serbianwordstrainer.logic

import com.mrdmitriy65.serbianwordstrainer.constants.Constants
import com.mrdmitriy65.serbianwordstrainer.data.WordPairDao
import com.mrdmitriy65.serbianwordstrainer.data.entities.WordPair
import com.mrdmitriy65.serbianwordstrainer.models.Exercise
import com.mrdmitriy65.serbianwordstrainer.models.ExercisePair
import com.mrdmitriy65.serbianwordstrainer.models.ExerciseType
import com.mrdmitriy65.serbianwordstrainer.models.TranslationType

class LearnStartedWordsExerciseGenerator(private val dao: WordPairDao) : IExerciseGenerator {

    override suspend fun generateExercises(): List<Exercise> {
        val words = getWords().shuffled()


        val result = mutableListOf<Exercise>()

        for (word in words) {
            val pronounce = if (word.pronunciation.isEmpty()) word.pronunciation else word.serbian
            val pair = ExercisePair(
                word.russian.trim(),
                word.serbian.trim(),
                TranslationType.DIRECT,
                pronounce
            )
            val pairReverse = ExercisePair(
                word.russian.trim(),
                word.serbian.trim(),
                TranslationType.REVERSE,
                pronounce
            )
            
            val type = GetExerciseType(word)
            when (type) {
                ExerciseType.CHOSE_FROM_VARIANTS -> {
                    result.add(Exercise(pairReverse, type, true))
                    result.add(Exercise(pair, type, false))
                    result.add(Exercise(pairReverse, type, false))
                }

                else -> {
                    result.add(Exercise(pair, type, true))
                    result.add(Exercise(pair, type, false))
                }
            }
        }

        return result.shuffled().toList()
    }

    private suspend fun getWords(): List<WordPair> {
        val words = dao.getWordPairsBetweenLevels(
            Constants.WORD_START_LEARN_LEVEL,
            Constants.WORD_LAST_LEARN_LEVEL
        )
        val result = mutableListOf<WordPair>()
        result.addAll(words)

        if (words.count() < Constants.EXERCISE_BUFFER_WORDS_COUNT) {
            val buffPotions =
                Constants.EXERCISE_BUFFER_WORDS_COUNT / Constants.EXERCISE_PORTION_ADD_TO_BUFFER_WORDS_COUNT
            val currentCountInPortions =
                words.count() / Constants.EXERCISE_PORTION_ADD_TO_BUFFER_WORDS_COUNT
            val difference = buffPotions - currentCountInPortions
            val toAddCount = difference * Constants.EXERCISE_PORTION_ADD_TO_BUFFER_WORDS_COUNT

            result.addAll(dao.getWordPairsNotStarted(toAddCount))
        }

        return result
            .shuffled()
            .take(Constants.EXERCISE_WORDS_COUNT_IN_EXERCISE)
            .toList()

    }

    private fun GetExerciseType(word: WordPair): ExerciseType {
        val level = word.learnLevel / Constants.WORD_ANSWERS_TO_INCREASE_EXERCISE_LEVEL
        return when (level) {
            0 -> ExerciseType.CHOSE_FROM_VARIANTS
            1 -> ExerciseType.WRITE_WORD_FROM_CHARACTERS
            else -> ExerciseType.WRITE_ANSWER
        }
    }

    override suspend fun generateWrongAnswers(
        wordsToExclude: List<String>,
        takeCount: Int
    ): List<WordPair> {
        return dao.getRandomWordsNotInRange(wordsToExclude, takeCount)
    }
}