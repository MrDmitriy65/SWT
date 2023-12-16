package com.mrdmitriy65.serbianwordstrainer.viewmodels

import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mrdmitriy65.serbianwordstrainer.data.WordPairDao
import com.mrdmitriy65.serbianwordstrainer.logic.IExerciseManager
import com.mrdmitriy65.serbianwordstrainer.logic.factories.ExerciseManagerFactory
import com.mrdmitriy65.serbianwordstrainer.models.Exercise

class TrainerViewModel(
    val wordPairDao: WordPairDao
) : ViewModel() {

    private lateinit var manager: IExerciseManager

    fun startFloatTraining(){
        manager = ExerciseManagerFactory().CreateExerciseManager(wordPairDao)
    }

    fun getCurrentExercise(): Exercise {
        return manager.getCurrentExercise()
    }

    fun getAnswers(): List<String> {
        return manager.getPossibleAnswers(getCurrentExercise())
    }

    fun setAnswer(answer: String) {
        manager.setAnswer(answer)
    }

    fun isTrainingComplete(): Boolean {
        return manager.isTrainingComplete()
    }

    fun setNextExercise() {
        manager.setNextExercise()
    }

    fun getTrainingResults(): List<Exercise> {
        return manager.getResults()
    }

    fun playQuestion(tts: TextToSpeech) {
        val exercise = manager.getCurrentExercise()
        var toPronounce: String?

        if (!exercise.pair.pronounce.isEmpty())
            toPronounce = exercise.pair.pronounce
        else if (!exercise.pair.question.isEmpty())
            toPronounce = exercise.pair.question
        else
            return

        if (exercise.isSpeakable) {
            tts.speak(
                toPronounce,
                TextToSpeech.QUEUE_FLUSH,
                Bundle(),
                toPronounce
            )
        }
    }

    fun completeTraining() {
        manager.completeTraining()
    }
}

class TrainerViewModelFactory(
    private val dao: WordPairDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TrainerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TrainerViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}