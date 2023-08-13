package com.mrdmitriy65.serbianwordstrainer.viewmodels

import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.mrdmitriy65.serbianwordstrainer.data.WordPairDao
import com.mrdmitriy65.serbianwordstrainer.data.entities.WordPair
import com.mrdmitriy65.serbianwordstrainer.logic.ExerciseManager
import com.mrdmitriy65.serbianwordstrainer.logic.IExerciseManager
import com.mrdmitriy65.serbianwordstrainer.models.Exercise
import com.mrdmitriy65.serbianwordstrainer.models.TranslatePair

class TrainerViewModel(
    wordPairDao: WordPairDao
) : ViewModel() {
    val allWords: LiveData<List<WordPair>> = wordPairDao.getAllWordPairs().asLiveData()

    private val manager: IExerciseManager = ExerciseManager()

    val wordsToLearn get() = manager.wordToLearn

    fun resetWords() {
        manager.resetWords()
    }

    fun setWordsToLearn(words: List<TranslatePair>, wordsToLearnCount: Int = 4) {
        manager.setWords(words, wordsToLearnCount)
    }

    fun configureManager() {
        manager.configureExercises()
    }

    fun getCurrentExercise(): Exercise {
        return manager.getExercise()
    }

    fun getAnswers(exercise: Exercise): List<String> {
        return manager.getAnswers(exercise)
    }

    fun setAnswer(answer: String) {
        manager.setAnswer(answer)
    }

    fun isTrainingComplete(): Boolean {
        return manager.isTrainigComplete()
    }

    fun setNextExercise() {
        manager.setNextExercise()
    }

    fun getTrainingResults(): List<Exercise> {
        return manager.getResults()
    }

    fun playQuestion(tts: TextToSpeech) {
        val exercise = manager.getExercise()
        if (exercise.isSpeakable) {
            tts.speak(
                exercise.pair.question,
                TextToSpeech.QUEUE_FLUSH,
                Bundle(),
                exercise.pair.question
            )
        }
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