package com.mrdmitriy65.serbianwordstrainer

import android.app.Application
import android.speech.tts.TextToSpeech
import com.mrdmitriy65.serbianwordstrainer.constants.Constants
import com.mrdmitriy65.serbianwordstrainer.data.AppDatabase

class SerbianWordsTrainerApplication : Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
    lateinit var tts: TextToSpeech

    private fun initializeTts() :TextToSpeech {
        val onInitListener = TextToSpeech.OnInitListener {
            if (it == TextToSpeech.SUCCESS) {
                tts.voice =
                    tts.voices?.find { it.name == Constants.SERBIAN_TTS_VOICE } ?: tts.defaultVoice
            }
        }
        return TextToSpeech(this.applicationContext, onInitListener)
    }

    override fun onCreate() {
        super.onCreate()

        tts = initializeTts()
    }
}