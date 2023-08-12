package com.mrdmitriy65.serbianwordstrainer.fragments.trainer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.mrdmitriy65.serbianwordstrainer.SerbianWordsTrainerApplication
import com.mrdmitriy65.serbianwordstrainer.adapters.WordTranslatePairAdapter
import com.mrdmitriy65.serbianwordstrainer.databinding.FragmentStartLearnBinding
import com.mrdmitriy65.serbianwordstrainer.models.ExerciseType
import com.mrdmitriy65.serbianwordstrainer.models.TranslatePair
import com.mrdmitriy65.serbianwordstrainer.viewmodels.TrainerViewModel
import com.mrdmitriy65.serbianwordstrainer.viewmodels.TrainerViewModelFactory

class StartLearnFragment : Fragment() {

    private var binding: FragmentStartLearnBinding? = null

    private val sharedViewModel: TrainerViewModel by activityViewModels{
        TrainerViewModelFactory(
            (activity?.application as SerbianWordsTrainerApplication)
                .database.wordPairDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentStartLearnBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        // Inflate the layout for this fragment
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            viewModel = sharedViewModel
            lifecycleOwner = viewLifecycleOwner
            startLearnFragment = this@StartLearnFragment
        }

        val adapter = WordTranslatePairAdapter()
        sharedViewModel.allWords.observe(this.viewLifecycleOwner){
            val toLearn = it.shuffled().map { TranslatePair(it.russian, it.serbian) }
            sharedViewModel.setWordsToLearn(toLearn)
            adapter.submitList(sharedViewModel.wordsToLearn)
        }

        binding?.exercisesList?.adapter = adapter
        binding?.resetButton?.setOnClickListener{
            sharedViewModel.resetWords()
            adapter.submitList(sharedViewModel.wordsToLearn)
        }
        binding?.startButton?.setOnClickListener{
            startTraining()
        }
    }

    private fun startTraining() {
        sharedViewModel.configureManager()
        val exercise = sharedViewModel.getCurrentExercise()
        val action = when(exercise.exerciseType) {
            ExerciseType.CHOSE_FROM_VARIANTS -> StartLearnFragmentDirections.actionStartLearnFragmentToChoseFromVariantsFragment()
            ExerciseType.WRITE_ANSWER -> StartLearnFragmentDirections.actionStartLearnFragmentToWriteAnswerExerciseFragment()
            ExerciseType.WRITE_WORD_FROM_CHARACTERS -> StartLearnFragmentDirections.actionStartLearnFragmentToWriteWordFromCharactersFragment()
        }
        this.findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}