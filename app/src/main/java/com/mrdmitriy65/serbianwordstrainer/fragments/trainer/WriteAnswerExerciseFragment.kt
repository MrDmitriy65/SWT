package com.mrdmitriy65.serbianwordstrainer.fragments.trainer

import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.mrdmitriy65.serbianwordstrainer.SerbianWordsTrainerApplication
import com.mrdmitriy65.serbianwordstrainer.databinding.FragmentWriteAnswerExerciseBinding
import com.mrdmitriy65.serbianwordstrainer.viewmodels.TrainerViewModel
import com.mrdmitriy65.serbianwordstrainer.viewmodels.TrainerViewModelFactory

class WriteAnswerExerciseFragment : Fragment() {

    private var _binding: FragmentWriteAnswerExerciseBinding? = null
    private lateinit var tts: TextToSpeech
    private val binding get() = _binding!!

    private val sharedViewModel: TrainerViewModel by activityViewModels{
        TrainerViewModelFactory(
            (activity?.application as SerbianWordsTrainerApplication)
                .database.wordPairDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        _binding = FragmentWriteAnswerExerciseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val exercise = sharedViewModel.getCurrentExercise()

        binding.nextExerciseButton.setOnClickListener { choseAnswer() }
        binding.skipExerciseButton.setOnClickListener { choseAnswer() }
        binding.answerFieldEditText.setOnKeyListener { viewParam, keyCode, _ ->
            handleKeyEvent(
                viewParam,
                keyCode
            )
        }

        tts = (activity?.application as SerbianWordsTrainerApplication).tts
        if (sharedViewModel.getCurrentExercise().isSpeakable) {
            binding.questionSound.visibility = View.VISIBLE
            binding.questionText.visibility = View.INVISIBLE
            binding.questionSound.setOnClickListener{
                playSound()
            }
            sharedViewModel.playQuestion(tts)
        }
        else {
            binding.questionText.text = exercise.pair.question
            binding.questionText.visibility = View.VISIBLE
            binding.questionSound.visibility = View.INVISIBLE
        }
    }

    private fun playSound() {
        sharedViewModel.playQuestion(tts)
    }

    private fun choseAnswer() {
        sharedViewModel.setAnswer(binding.answerField.editText?.text?.toString() ?: "")
        val action = WriteAnswerExerciseFragmentDirections.actionWriteAnswerExerciseFragmentToAnswerFragment();
        this.findNavController().navigate(action)
    }

    private fun handleKeyEvent(view: View, keyCode: Int): Boolean {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            // Hide the keyboard
            val inputMethodManager =
                activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            choseAnswer()
            return true
        }
        return false
    }
}