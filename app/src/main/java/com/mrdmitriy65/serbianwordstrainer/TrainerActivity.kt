package com.mrdmitriy65.serbianwordstrainer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.mrdmitriy65.serbianwordstrainer.constants.Constants.Companion.SERBIAN_TTS_VOICE
import com.mrdmitriy65.serbianwordstrainer.databinding.ActivityTrainerBinding

class TrainerActivity : AppCompatActivity(), ITts {
    private lateinit var binding: ActivityTrainerBinding
    private lateinit var navController: NavController
    private lateinit var tts: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrainerBinding.inflate(layoutInflater)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.trainer_nav_host_container) as NavHostFragment
        navController = navHostFragment.navController

        initializeTts()

        setContentView(binding.root)
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