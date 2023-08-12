package com.mrdmitriy65.serbianwordstrainer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import com.mrdmitriy65.serbianwordstrainer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var tts: TextToSpeech
    var counter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.moveToCategoriesButton.setOnClickListener { moveToTraining() }
        binding.moveToDictionaryButton.setOnClickListener { moveToDictionary() }
        binding.moveToExperimentalButton.setOnClickListener {
            moveToExperimentalSpeach()
            //moveToTraining()
        }

        val onInitListener = TextToSpeech.OnInitListener {
//            tts.setOnUtteranceProgressListener(progressListener)
            tts.voice = tts.voices.find { it.name == "sr" } ?: tts.defaultVoice
            //tts.speak("1 2 3", TextToSpeech.QUEUE_ADD, Bundle(), "utteranceId")
        }
        tts = TextToSpeech(this, onInitListener)

    }

    private fun moveToDictionary(){
        val intent = Intent(this, com.mrdmitriy65.serbianwordstrainer.DictionaryActivity::class.java)
        startActivity(intent)
    }

    private fun moveToTraining(){
        val intent = Intent(this, com.mrdmitriy65.serbianwordstrainer.TrainerActivity::class.java)
        startActivity(intent)
    }

    private fun moveToExperimentalSpeach() {
        //val intent = Intent(this, TrainerActivity::class.java)
        //startActivity(intent)
        tts.voice = tts.voices.find { it.name == "sr" } ?: tts.defaultVoice
        val ttt = tts.voices.find { it.name.startsWith("sr") }
        binding.textView.text = "${tts.voice.name} \n ${ttt?.name} \n ${tts.voices.count()}"

        tts.speak(counter.toString(), TextToSpeech.QUEUE_ADD, Bundle(), "utteranceId")
        counter++
    }
}