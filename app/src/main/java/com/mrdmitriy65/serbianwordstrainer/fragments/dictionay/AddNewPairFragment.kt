package com.mrdmitriy65.serbianwordstrainer.fragments.dictionay

import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.mrdmitriy65.serbianwordstrainer.R
import com.mrdmitriy65.serbianwordstrainer.SerbianWordsTrainerApplication
import com.mrdmitriy65.serbianwordstrainer.databinding.FragmentAddNewPairBinding
import com.mrdmitriy65.serbianwordstrainer.viewmodels.DictionaryViewModel
import com.mrdmitriy65.serbianwordstrainer.viewmodels.DictionaryViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mrdmitriy65.serbianwordstrainer.constants.Constants

class AddNewPairFragment : Fragment() {
    private val navigationArgs: AddNewPairFragmentArgs by navArgs()
    private lateinit var tts: TextToSpeech

    private var _binding: FragmentAddNewPairBinding? = null
    private val binding get() = _binding!!

    var selectedCategoryId: Int = -1

    private val viewModel: DictionaryViewModel by activityViewModels {
        DictionaryViewModelFactory(
            (activity?.application as SerbianWordsTrainerApplication)
                .database.wordPairDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddNewPairBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.allCategories
            .observe(this.viewLifecycleOwner) {
                binding.apply {
                    // TODO добавить категорию по умолчанию
                    val adapter =
                        ArrayAdapter(
                            this@AddNewPairFragment.requireContext(),
                            android.R.layout.simple_spinner_item,
                            it.map { x -> x.name }
                        )
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    categories.adapter = adapter
                    categories.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                p0: AdapterView<*>?,
                                p1: View?,
                                p2: Int,
                                p3: Long
                            ) {
                                selectedCategoryId = it[p2].id
                            }

                            override fun onNothingSelected(p0: AdapterView<*>?) {
                                selectedCategoryId = -1
                            }
                        }
                    val startCategory = it.find { x -> x.id == navigationArgs.categoryId }
                    val position = it.indexOf(startCategory)
                    categories.setSelection(position)

                    addNewCategory.setOnClickListener {
                        addNewCategoryDialog()
                    }
                }
            }
        if (navigationArgs.wordPairId > 0) {
            bindEditPair()
        } else {
            bindAddNewPair()
        }
        binding.playWord.setOnClickListener { playSound() }

        tts = (activity?.application as SerbianWordsTrainerApplication).tts
    }

    private fun addNewCategoryDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.add_new_pair_fragment_add_new_category))
            .setView(R.layout.dialog_edittext)
            .setNegativeButton(getString(R.string.button_cancel)) { _, _ -> }
            .setPositiveButton(getString(R.string.button_add)) { dialog, _ ->
                val et = (dialog as AlertDialog)
                    .findViewById<EditText>(R.id.new_category_name)
                val newName = et?.text.toString().trim()
                if (newName.isEmpty()) {
                    showToast(getString(R.string.add_new_pair_fragment_empty_category_name))
                } else {
                    viewModel.addNewCategory(newName)
                }
            }
            .show()
    }

    private fun bindAddNewPair() {
        binding.save.setOnClickListener {
            addNewPair()
        }
        binding.wordLearnLevel.visibility = View.INVISIBLE
    }

    private fun bindEditPair() {
        viewModel.getPairById(navigationArgs.wordPairId).observe(this.viewLifecycleOwner) {
            binding.apply {
                russianWord.setText(it.russian, TextView.BufferType.SPANNABLE)
                serbianWord.setText(it.serbian, TextView.BufferType.SPANNABLE)
                comment.setText(it.comment, TextView.BufferType.SPANNABLE)
                pronunciation.setText(it.pronunciation, TextView.BufferType.SPANNABLE)
                wordLearnLevel.text = getString(
                    R.string.add_new_pair_fragment_learn_level_of,
                    it.learnLevel.toString(),
                    Constants.WORD_LAST_LEARN_LEVEL.toString()
                )
            }
        }
        binding.save.setOnClickListener {

            if (viewModel.isPairValidForChange(
                    navigationArgs.wordPairId,
                    binding.russianWord.text.toString(),
                    binding.serbianWord.text.toString()
                )
            ) {
                viewModel.updatePair(
                    navigationArgs.wordPairId,
                    binding.russianWord.text.toString(),
                    binding.serbianWord.text.toString(),
                    binding.comment.text.toString(),
                    selectedCategoryId,
                    binding.pronunciation.text.toString()
                )
                showToast(getString(R.string.text_changes_saved))
                val action =
                    AddNewPairFragmentDirections.actionAddNewPairFragmentToWordPairsListFragment(
                        selectedCategoryId
                    )
                findNavController().navigate(action)
            } else {
                showToast(getString(R.string.add_new_pair_fragment_can_not_save_pair))
            }
        }

    }


    private fun addNewPair() {
        if (isPairValid()) {
            viewModel.addNewPair(
                binding.russianWord.text.toString(),
                binding.serbianWord.text.toString(),
                selectedCategoryId,
                binding.comment.text.toString(),
                binding.pronunciation.text.toString()
            )
            showToast(getString(R.string.add_new_pair_fragment_pair_added))

            binding.russianWord.text.clear()
            binding.serbianWord.text.clear()
            binding.comment.text.clear()
        } else {
            showToast(getString(R.string.add_new_pair_fragment_data_is_not_correct))
        }
    }

    private fun isPairValid(): Boolean {
        return viewModel.isPairValid(
            binding.russianWord.text.toString(),
            binding.serbianWord.text.toString(),
            selectedCategoryId
        )
    }

    private fun showToast(text: String) {
        Toast
            .makeText(this.requireContext(), text, Toast.LENGTH_SHORT)
            .show()
    }

    fun playSound() {
        var toPronounce: String?

        if (!binding.pronunciation.text.toString().isEmpty())
            toPronounce = binding.pronunciation.text.toString()
        else if (!binding.serbianWord.text.toString().isEmpty())
            toPronounce = binding.serbianWord.text.toString()
        else
            return

        tts.speak(
            toPronounce,
            TextToSpeech.QUEUE_FLUSH,
            Bundle(),
            toPronounce
        )
    }
}