package com.mrdmitriy65.serbianwordstrainer.fragments.trainer

import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.mrdmitriy65.serbianwordstrainer.SerbianWordsTrainerApplication
import com.mrdmitriy65.serbianwordstrainer.adapters.CharactersButtonAdapter
import com.mrdmitriy65.serbianwordstrainer.databinding.FragmentWriteWordFromCharactersBinding
import com.mrdmitriy65.serbianwordstrainer.viewmodels.TrainerViewModel
import com.mrdmitriy65.serbianwordstrainer.viewmodels.TrainerViewModelFactory

class WriteWordFromCharactersFragment : Fragment() {
    private var _binding: FragmentWriteWordFromCharactersBinding? = null
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
        _binding = FragmentWriteWordFromCharactersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val exercise = sharedViewModel.getCurrentExercise()

        val adapter = CharactersButtonAdapter(exercise.pair.answer, this::onCompleteExercise, this::onAnswerChanged)

        binding.questionText.text = exercise.pair.question
        binding.characterList.adapter = adapter
        binding.characterList.layoutManager = GridLayoutManager(requireContext(), 5)

        binding.skipExerciseButton.setOnClickListener { skipExercise() }

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

    private fun skipExercise() {
        sharedViewModel.setAnswer(binding.answerText.text.toString())
        val action = WriteWordFromCharactersFragmentDirections.actionWriteWordFromCharactersFragmentToAnswerFragment()
        this.findNavController().navigate(action)
    }

    private fun onCompleteExercise(answer: String?) {
        sharedViewModel.setAnswer(answer ?: "")
        val action = WriteWordFromCharactersFragmentDirections.actionWriteWordFromCharactersFragmentToAnswerFragment()
        this.findNavController().navigate(action)
    }

    private fun onAnswerChanged(answeredPart: String){
        binding.answerText.text = answeredPart
    }
}