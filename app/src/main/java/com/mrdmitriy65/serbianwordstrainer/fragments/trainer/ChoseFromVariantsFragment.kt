package com.mrdmitriy65.serbianwordstrainer.fragments.trainer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.mrdmitriy65.serbianwordstrainer.SerbianWordsTrainerApplication
import com.mrdmitriy65.serbianwordstrainer.TrainerActivity
import com.mrdmitriy65.serbianwordstrainer.databinding.FragmentChoseFromVariantsBinding
import com.mrdmitriy65.serbianwordstrainer.viewmodels.TrainerViewModel
import com.mrdmitriy65.serbianwordstrainer.viewmodels.TrainerViewModelFactory

/**
 * A simple [Fragment] subclass.
 * Use the [ChoseFromVariantsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChoseFromVariantsFragment : Fragment() {

    private var binding: FragmentChoseFromVariantsBinding? = null

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
        val fragmentBinding = FragmentChoseFromVariantsBinding.inflate(inflater, container, false)
        binding = fragmentBinding

        // Inflate the layout for this fragment
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            viewModel = sharedViewModel
            lifecycleOwner = viewLifecycleOwner
            chooseFromVariantFragment = this@ChoseFromVariantsFragment

            val exercise = sharedViewModel.getCurrentExercise()
            val answers = sharedViewModel.getAnswers(exercise)

            if (exercise.isSpeakable) {
                questionText.visibility = View.INVISIBLE
                questionSound.visibility = View.VISIBLE
                questionSound.setOnClickListener {
                    val tts = (activity as TrainerActivity).getTestToSpeach()
                    sharedViewModel.playQuestion(tts)
                }
                sharedViewModel.playQuestion((activity as TrainerActivity).getTestToSpeach())
            } else {
                questionText.text = exercise.pair.question
                questionText.visibility = View.VISIBLE
                questionSound.visibility = View.INVISIBLE
            }

            answer1.text = answers[0]
            answer2.text = answers[1]
            answer3.text = answers[2]
            answer4.text = answers[3]

            answer1.setOnClickListener { choseAnser(answer1.text.toString()) }
            answer2.setOnClickListener { choseAnser(answer2.text.toString()) }
            answer3.setOnClickListener { choseAnser(answer3.text.toString()) }
            answer4.setOnClickListener { choseAnser(answer4.text.toString()) }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun choseAnser(answer: String) {
        sharedViewModel.setAnswer(answer)
        val action =
            ChoseFromVariantsFragmentDirections.actionChoseFromVariantsFragmentToAnswerFragment();
        this.findNavController().navigate(action)
    }
}