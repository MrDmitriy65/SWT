package com.mrdmitriy65.serbianwordstrainer.fragments.trainer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.mrdmitriy65.serbianwordstrainer.R
import com.mrdmitriy65.serbianwordstrainer.SerbianWordsTrainerApplication
import com.mrdmitriy65.serbianwordstrainer.adapters.ResultListAdapter
import com.mrdmitriy65.serbianwordstrainer.databinding.FragmentTrainingResultBinding
import com.mrdmitriy65.serbianwordstrainer.viewmodels.TrainerViewModel
import com.mrdmitriy65.serbianwordstrainer.viewmodels.TrainerViewModelFactory

class TrainingResultFragment : Fragment() {

    private var _binding: FragmentTrainingResultBinding? = null
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
        _binding = FragmentTrainingResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val results = sharedViewModel.getTrainingResults()
        val adapter = ResultListAdapter(results)

        binding.resultList.adapter = adapter
        binding.resultList.setHasFixedSize(true)

        binding.resultText.text = getString(R.string.exercise_results,
            results.count { it.isCorrect }.toString(),
            results.count { it.isCompleted }.toString()
        )
    }
}