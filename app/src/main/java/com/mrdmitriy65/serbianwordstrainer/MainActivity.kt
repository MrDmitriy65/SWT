package com.mrdmitriy65.serbianwordstrainer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mrdmitriy65.serbianwordstrainer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.moveToCategoriesButton.setOnClickListener { moveToTraining() }
        binding.moveToDictionaryButton.setOnClickListener { moveToDictionary() }
        binding.moveToExperimentalButton.setOnClickListener {
        }
    }

    private fun moveToDictionary(){
        val intent = Intent(this, DictionaryActivity::class.java)
        startActivity(intent)
    }

    private fun moveToTraining(){
        val intent = Intent(this, TrainerActivity::class.java)
        startActivity(intent)
    }
}