package com.mrdmitriy65.serbianwordstrainer.logic

import com.mrdmitriy65.serbianwordstrainer.models.*
import com.mrdmitriy65.serbianwordstrainer.models.TranslatePair

class ExerciseManager(
) : IExerciseManager {

    override var wordToLearn: List<TranslatePair> = listOf()

    private var wordDictionary: List<TranslatePair> = listOf()
    private var wordsToLearnCount: Int = 0

    private val exerciseList: MutableList<Exercise> = mutableListOf()
    private var currentExerciseNumber: Int = -1

    override fun getExercise(): Exercise {
        return exerciseList[currentExerciseNumber]
    }

    private fun resetState() {
        exerciseList.clear()
        currentExerciseNumber = -1
    }

    override fun configureExercises() {
        resetState()
        for (pair in wordToLearn) {
            val exercise =
                ExercisePair(pair.word.trim(), pair.translate.trim(), TranslationType.DIRECT)
            val exerciseReverse =
                ExercisePair(pair.word.trim(), pair.translate.trim(), TranslationType.REVERSE)

            exerciseList.add(Exercise(exercise, ExerciseType.CHOSE_FROM_VARIANTS))
            exerciseList.add(Exercise(exerciseReverse, ExerciseType.CHOSE_FROM_VARIANTS, true))
            exerciseList.add(Exercise(exerciseReverse, ExerciseType.CHOSE_FROM_VARIANTS))
            exerciseList.add(Exercise(exercise, ExerciseType.WRITE_ANSWER))
            exerciseList.add(Exercise(exercise, ExerciseType.WRITE_WORD_FROM_CHARACTERS))
        }
        exerciseList.shuffle()
        currentExerciseNumber++
    }

    override fun setAnswer(userAnswer: String?) {
        with(exerciseList[currentExerciseNumber])
        {
            val editedAnswer = modifyAnswer(pair.answer)
            val editedUserAnswer = modifyAnswer(userAnswer ?: "")

            isCompleted = true
            isCorrect = editedAnswer.equals(editedUserAnswer, true)
        }
    }

    override fun setNextExercise() {
        currentExerciseNumber++
    }

    override fun isTrainigComplete(): Boolean {
        return currentExerciseNumber >= exerciseList.count()
    }

    private fun modifyAnswer(answer: String): String = answer.lowercase().replace('ё', 'е').trim()

    override fun getAnswers(exercise: Exercise): List<String> {
        return wordDictionary.filter { x ->
            !wordToLearn.any { y ->
                x.word.trim() == y.word.trim() || x.translate.trim() == y.translate.trim()
            }
        }
            .shuffled()
            .take(3)
            .map {
                when (exercise.pair.translationType) {
                    TranslationType.DIRECT -> it.translate
                    else -> it.word
                }
            }
            .union(listOf(exercise.pair.answer))
            .toList()
            .shuffled()
    }

    override fun getResults(): List<Exercise> {
        return exerciseList.toList()
    }

    override fun setWords(wordDictionary: List<TranslatePair>, wordsToLearnCount: Int) {
        this.wordDictionary = wordDictionary
        this.wordsToLearnCount = wordsToLearnCount

        resetWords()
    }

    override fun resetWords() {
        wordToLearn = wordDictionary.shuffled().take(wordsToLearnCount)
    }
}