package com.mrdmitriy65.serbianwordstrainer.logic

import com.mrdmitriy65.serbianwordstrainer.models.Exercise
import com.mrdmitriy65.serbianwordstrainer.models.TranslatePair
import com.mrdmitriy65.serbianwordstrainer.models.TranslationType
import kotlinx.coroutines.runBlocking
import kotlin.random.Random

class FloatLearnExerciseManager(private val exerciseGenerator: IExerciseGenerator) :
    IExerciseManager {
    private val exercises = runBlocking { return@runBlocking exerciseGenerator.generateExercises() }

    private val learningExercises = exercises.toMutableList()
    private var currentExerciseIndex = 0

    override val wordToLearn: List<TranslatePair>
        get() = TODO("Not yet implemented")

    override fun getCurrentExercise(): Exercise {
        // TODO add index range check
        return learningExercises[currentExerciseIndex]
    }

    override fun configureExercises() {
        // TODO deprecated
        TODO("Not yet implemented")
    }

    override fun setAnswer(userAnswer: String?) {
        with(learningExercises[currentExerciseIndex])
        {
            val editedAnswer = clearString(pair.answer)
            val editedUserAnswer = clearString(userAnswer ?: "")

            isCompleted = true
            isCorrect = editedAnswer.equals(editedUserAnswer, true)

            if (!isCorrect && learningExercises.count { x -> x.pair == pair } < 2) {
                val newExercise = Exercise(pair, exerciseType, isSpeakable)
                val newIndex = Random.nextInt(currentExerciseIndex, learningExercises.count())
                learningExercises.add(newIndex, newExercise)
            }
        }
    }

    private fun clearString(answer: String): String = answer.lowercase().replace('ё', 'е').trim()

    override fun setNextExercise() {
        currentExerciseIndex++
    }

    override fun isTrainingComplete(): Boolean {
        return learningExercises.all { x -> x.isCompleted }
    }

    override fun getPosibleAnswers(exercise: Exercise): List<String> {
        val result = mutableListOf<String>()
        val answers = runBlocking {
            val wrongAnswers = exerciseGenerator.generateWrongAnswers(
                listOf(
                    exercise.pair.answer,
                    exercise.pair.question
                ), 3
            )
            if (exercise.pair.translationType == TranslationType.DIRECT) {
                return@runBlocking wrongAnswers.map { it.serbian }
            } else {
                return@runBlocking wrongAnswers.map { it.russian }
            }
        }
        result.addAll(answers)
        result.add(exercise.pair.answer)
        return result.shuffled().toList()
    }

    override fun getResults(): List<Exercise> {
        return learningExercises.toList()
    }

    override fun setWords(wordDictionary: List<TranslatePair>, wordsToLearnCount: Int) {
        // TODO deprecated
        TODO("Not yet implemented")
    }

    override fun resetWords() {
        // TODO deprecated
        TODO("Not yet implemented")
    }


}