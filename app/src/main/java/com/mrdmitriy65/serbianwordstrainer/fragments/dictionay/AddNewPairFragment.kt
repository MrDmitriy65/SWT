package com.mrdmitriy65.serbianwordstrainer.fragments.dictionay

import android.os.Bundle
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

class AddNewPairFragment : Fragment() {
    private val navigationArgs: AddNewPairFragmentArgs by navArgs()

    private var _binding: FragmentAddNewPairBinding? = null
    private val binding get() = _binding!!

    var _selectedCategoryId: Int = -1

    private val viewModel: DictionaryViewModel by activityViewModels {
        DictionaryViewModelFactory(
            (activity?.application as SerbianWordsTrainerApplication)
                .database.wordPairDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddNewPairBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.allCategories
            .observe(this.viewLifecycleOwner) {
                binding.apply {
                    // TODO добавить категорию по умолчанию, которую нельзя выбрать
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
                                _selectedCategoryId = it[p2].id
                            }

                            override fun onNothingSelected(p0: AdapterView<*>?) {
                                _selectedCategoryId = -1
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
    }

    private fun addNewCategoryDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Введите название категории")
            .setView(R.layout.dialog_edittext)
            .setNegativeButton("Отмена") { _, _ -> }
            .setPositiveButton("Добавить") { dialog, _ ->
                    val et = (dialog as AlertDialog)
                        .findViewById<EditText>(R.id.new_category_name)
                    val newName = et?.text.toString().trim()
                    if(newName.isEmpty() == true){
                        showToast("Пустое название категории")
                    }
                    else {
                        viewModel.addNewCategory(newName)
                    }
            }
            .show()
    }

    private fun bindAddNewPair() {
        binding.save.setOnClickListener {
            addNewPair()
        }
    }

    private fun bindEditPair() {
        viewModel.getPairById(navigationArgs.wordPairId).observe(this.viewLifecycleOwner) {
            binding.apply {
                russianWord.setText(it.russian, TextView.BufferType.SPANNABLE)
                serbianWord.setText(it.serbian, TextView.BufferType.SPANNABLE)
                comment.setText(it.comment, TextView.BufferType.SPANNABLE)
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
                    _selectedCategoryId
                )
                showToast("Изменения сохранены")
                val action =
                    AddNewPairFragmentDirections.actionAddNewPairFragmentToWordPairsListFragment(
                        _selectedCategoryId
                    )
                findNavController().navigate(action)
            } else {
                showToast("Нельзя сохранить пару")
            }
        }

    }


    fun addNewPair() {
        if (isPairValid()) {
            viewModel.addNewPair(
                binding.russianWord.text.toString(),
                binding.serbianWord.text.toString(),
                _selectedCategoryId,
                binding.comment.text.toString()
            )
            showToast("Пара добавлена")

            binding.russianWord.text.clear()
            binding.serbianWord.text.clear()
            binding.comment.text.clear()
        } else {
            showToast("Данные неверны")
        }
    }

    private fun isPairValid(): Boolean {
        return viewModel.isPairValid(
            binding.russianWord.text.toString(),
            binding.serbianWord.text.toString(),
            _selectedCategoryId
        )
    }

    private fun showToast(text: String) {
        Toast
            .makeText(this.requireContext(), text, Toast.LENGTH_SHORT)
            .show()
    }
}