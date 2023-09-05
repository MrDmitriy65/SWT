package com.mrdmitriy65.serbianwordstrainer.fragments.trainer

import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.mrdmitriy65.serbianwordstrainer.SerbianWordsTrainerApplication
import com.mrdmitriy65.serbianwordstrainer.databinding.FragmentChoseFromVariantsBinding
import com.mrdmitriy65.serbianwordstrainer.viewmodels.TrainerViewModel
import com.mrdmitriy65.serbianwordstrainer.viewmodels.TrainerViewModelFactory

class ChoseFromVariantsFragment : Fragment() {

    private var binding: FragmentChoseFromVariantsBinding? = null
    private lateinit var tts: TextToSpeech

    private val sharedViewModel: TrainerViewModel by activityViewModels {
        TrainerViewModelFactory(
            (activity?.application as SerbianWordsTrainerApplication)
                .database.wordPairDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragmentBinding = FragmentChoseFromVariantsBinding.inflate(inflater, container, false)
        binding = fragmentBinding

        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            viewModel = sharedViewModel
            lifecycleOwner = viewLifecycleOwner
            chooseFromVariantFragment = this@ChoseFromVariantsFragment
            answers = sharedViewModel.getAnswers()
        }

        tts = (activity?.application as SerbianWordsTrainerApplication).tts
        if (sharedViewModel.getCurrentExercise().isSpeakable) {
            sharedViewModel.playQuestion(tts)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    fun choseAnswer(answer: String) {
        sharedViewModel.setAnswer(answer)
        val action =
            ChoseFromVariantsFragmentDirections.actionChoseFromVariantsFragmentToAnswerFragment();
        this.findNavController().navigate(action)
    }

    fun playSound() {
        sharedViewModel.playQuestion(tts)
    }
}