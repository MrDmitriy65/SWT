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
            val pronounce = word.pronunciation.ifEmpty { word.serbian }
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

        // Fill buffer if needed
        if (result.count() < Constants.EXERCISE_BUFFER_WORDS_COUNT) {
            var skip = 0;
            var take = Constants.EXERCISE_BUFFER_WORDS_COUNT - result.count()
            while (take > 0) {
                val tempWords =
                    dao.getWordPairsWithLevel(Constants.WORD_NOT_STARTED_LEARN, skip, take)
                for (word in tempWords) {
                    // If exists word or translate than skip
                    val isExists = result.any { x ->
                        x.serbian == word.serbian ||
                        x.russian == word.russian
                    }
                    if(!isExists){
                        result.add(word)
                        dao.increasePairLevel(word.id)
                    }
                }
                // Calculate next part of words to load
                skip += take
                take = if(tempWords.count() != take) 0 else Constants.EXERCISE_BUFFER_WORDS_COUNT - result.count()
            }
        }

        return result
            .shuffled()
            .take(Constants.EXERCISE_WORDS_COUNT_IN_EXERCISE)
            .toList()

    }

    private fun GetExerciseType(word: WordPair): ExerciseType {
        // Minus 1 because 1 is first level to start learn
        val level = (word.learnLevel -1) / Constants.WORD_ANSWERS_TO_INCREASE_EXERCISE_LEVEL
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