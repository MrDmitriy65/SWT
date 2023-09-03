package com.mrdmitriy65.serbianwordstrainer

import android.speech.tts.TextToSpeech

interface ITts {
    fun getTextToSpeech(): TextToSpeech
}