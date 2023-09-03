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

    private var _binding: FragmentStartLearnBinding? = null

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
        _binding = FragmentStartLearnBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = WordTranslatePairAdapter()
        sharedViewModel.allWords.observe(this.viewLifecycleOwner){
            val toLearn = it.shuffled().map { TranslatePair(it.russian, it.serbian, it.pronunciation) }
            sharedViewModel.setWordsToLearn(toLearn)
            adapter.submitList(sharedViewModel.wordsToLearn)
        }

        binding.exercisesList.adapter = adapter
        binding.resetButton.setOnClickListener{
            sharedViewModel.resetWords()
            adapter.submitList(sharedViewModel.wordsToLearn)
        }
        binding.startButton.setOnClickListener{
            startTraining()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
}