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
        val words = getWords()


        val result = mutableListOf<Exercise>()

        for (word in words.shuffled()){
            val pair = ExercisePair(word.russian.trim(), word.serbian.trim(), TranslationType.DIRECT)
            val pairReverse = ExercisePair(word.russian.trim(), word.serbian.trim(), TranslationType.REVERSE, word.serbian)
            val type = GetExerciseType(word)


            if (type == ExerciseType.CHOSE_FROM_VARIANTS){
                result.add(Exercise(pairReverse, type, true))
                result.add(Exercise(pair, type, false))
                result.add(Exercise(pairReverse, type, false))
            }
            else {
                result.add(Exercise(pair, type, false))
            }
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

            return (words + dao.getWordPairsNotStarted(toAddCount)).take(Constants.EXERCISE_WORDS_COUNT_IN_EXERCISE)
        }

        if (words.count() > Constants.EXERCISE_BUFFER_WORDS_COUNT)
        {
            return words.shuffled().take(Constants.EXERCISE_WORDS_COUNT_IN_EXERCISE)
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

    override suspend fun generateWrongAnswers(words: List<String>, takeCount:Int): List<WordPair> {
        return dao.getRandomWordsNotInRange(words, takeCount)
    }
}