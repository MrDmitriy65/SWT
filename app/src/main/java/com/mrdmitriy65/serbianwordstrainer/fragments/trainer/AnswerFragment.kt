package com.mrdmitriy65.serbianwordstrainer.fragments.trainer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.mrdmitriy65.serbianwordstrainer.R
import com.mrdmitriy65.serbianwordstrainer.SerbianWordsTrainerApplication
import com.mrdmitriy65.serbianwordstrainer.databinding.FragmentAnswerBinding
import com.mrdmitriy65.serbianwordstrainer.models.ExerciseType
import com.mrdmitriy65.serbianwordstrainer.viewmodels.TrainerViewModel
import com.mrdmitriy65.serbianwordstrainer.viewmodels.TrainerViewModelFactory

class AnswerFragment : Fragment() {

    private var binding: FragmentAnswerBinding? = null

    private val sharedViewModel: TrainerViewModel by activityViewModels {
        TrainerViewModelFactory(
            (activity?.application as SerbianWordsTrainerApplication)
                .database.wordPairDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentAnswerBinding.inflate(inflater, container, false)
        binding = fragmentBinding

        // Inflate the layout for this fragment
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            viewModel = sharedViewModel
            lifecycleOwner = viewLifecycleOwner
            answerFragment = this@AnswerFragment
        }
    }

    fun moveNext() {
        sharedViewModel.setNextExercise()
        val action = if (sharedViewModel.isTrainingComplete()) {
            AnswerFragmentDirections.actionAnswerFragmentToTrainingResultFragment()
        } else {
            val exercise = sharedViewModel.getCurrentExercise()
            when (exercise.exerciseType) {
                ExerciseType.CHOSE_FROM_VARIANTS -> AnswerFragmentDirections.actionAnswerFragmentToChoseFromVariantsFragment()
                ExerciseType.WRITE_ANSWER -> AnswerFragmentDirections.actionAnswerFragmentToWriteAnswerExerciseFragment()
                ExerciseType.WRITE_WORD_FROM_CHARACTERS -> AnswerFragmentDirections.actionAnswerFragmentToWriteWordFromCharactersFragment()
            }
        }
        this.findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}