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
import com.mrdmitriy65.serbianwordstrainer.logic.LearnStartedWordsExerciseGenerator
import com.mrdmitriy65.serbianwordstrainer.logic.factories.ExerciseManagerFactory
import com.mrdmitriy65.serbianwordstrainer.models.Exercise
import com.mrdmitriy65.serbianwordstrainer.models.TranslatePair
import kotlinx.coroutines.runBlocking

class TrainerViewModel(
    val wordPairDao: WordPairDao
) : ViewModel() {
    val allWords: LiveData<List<WordPair>> = wordPairDao.getAllWordPairs().asLiveData()

    private var manager: IExerciseManager = ExerciseManager()

    val wordsToLearn get() = manager.wordToLearn

    // TODO remove in new version
    fun resetWords() {
        manager = ExerciseManager()
        manager.resetWords()
    }

    fun startFloat(){
        manager = ExerciseManagerFactory().CreateExerciseManager(wordPairDao)
    }

    // TODO remove in new version
    fun setWordsToLearn(words: List<TranslatePair>, wordsToLearnCount: Int = 4) {
        manager.setWords(words, wordsToLearnCount)
    }

    // TODO remove in new version
    fun configureManager() {
        manager.configureExercises()
    }

    fun getCurrentExercise(): Exercise {
        return manager.getCurrentExercise()
    }

    fun getAnswers(): List<String> {
        return manager.getPosibleAnswers(getCurrentExercise())
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