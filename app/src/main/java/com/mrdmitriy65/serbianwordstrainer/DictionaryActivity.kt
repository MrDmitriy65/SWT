package com.mrdmitriy65.serbianwordstrainer

import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.mrdmitriy65.serbianwordstrainer.constants.Constants.Companion.SERBIAN_TTS_VOICE

class DictionaryActivity : AppCompatActivity(R.layout.activity_dictionary), ITts {

    private lateinit var navController: NavController
    private lateinit var tts: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initializeTts()
        fun getTextToSpeach(): TextToSpeech {
            return tts
        }

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.dictionary_nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }

    private fun initializeTts() {
        val onInitListener = TextToSpeech.OnInitListener {
            if (it == TextToSpeech.SUCCESS) {
                tts.voice =
                    tts.voices?.find { it.name == SERBIAN_TTS_VOICE } ?: tts.defaultVoice
            }
        }
        tts = TextToSpeech(this, onInitListener)
    }

    override fun getTextToSpeech(): TextToSpeech {
        return tts
    }
}