package com.mrdmitriy65.serbianwordstrainer

import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.mrdmitriy65.serbianwordstrainer.constants.Constants.Companion.SERBIAN_TTS_VOICE

class DictionaryActivity : AppCompatActivity(R.layout.activity_dictionary) {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.dictionary_nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }
}