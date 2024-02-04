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

            val type = getExerciseType(word)
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

        return result.shuffled().splitDuplicatedPairs()
    }

    private fun List<Exercise>.splitDuplicatedPairs() : List<Exercise>
    {
        val mutableSource = this.toMutableList()
        var index = 0
        while (index < mutableSource.count() -1)
        {
            if(mutableSource[index].pair == mutableSource[index + 1].pair)
            {
                val toExchange = mutableSource[index]
                mutableSource.removeAt(index)
                val insertIndex = findFreeIndexFor(toExchange.pair, mutableSource)
                mutableSource.add(insertIndex, toExchange)
                continue
            }
            index++
        }
        return mutableSource.toList()
    }

    private fun findFreeIndexFor(pair: ExercisePair, source: MutableList<Exercise>): Int {
        for (i in 0 until source.count())
        {
            if ((i == 0 || source[i - 1].pair != pair) && source[i].pair != pair)
            {
                return i
            }
        }

        return 0
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
            var skip = 0
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

    private fun getExerciseType(word: WordPair): ExerciseType {
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