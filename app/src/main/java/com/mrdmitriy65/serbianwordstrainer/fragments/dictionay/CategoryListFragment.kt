package com.mrdmitriy65.serbianwordstrainer.fragments.dictionay

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mrdmitriy65.serbianwordstrainer.R
import com.mrdmitriy65.serbianwordstrainer.SerbianWordsTrainerApplication
import com.mrdmitriy65.serbianwordstrainer.adapters.CategoryListAdapter_new
import com.mrdmitriy65.serbianwordstrainer.data.entities.Category
import com.mrdmitriy65.serbianwordstrainer.databinding.FragmentCategoryListBinding
import com.mrdmitriy65.serbianwordstrainer.helpers.SwipeHelper
import com.mrdmitriy65.serbianwordstrainer.viewmodels.DictionaryViewModel
import com.mrdmitriy65.serbianwordstrainer.viewmodels.DictionaryViewModelFactory

class CategoryListFragment : Fragment() {
    private var _binding: FragmentCategoryListBinding? = null
    private val binding get() = _binding!!

    private lateinit var categories: List<Category>

    private val viewModel: DictionaryViewModel by activityViewModels {
        DictionaryViewModelFactory(
            (activity?.application as SerbianWordsTrainerApplication).database.wordPairDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = CategoryListAdapter_new {
            val action = CategoryListFragmentDirections
                .actionCategoryListFragmentToWordPairsListFragment(it.id)
            this.findNavController().navigate(action)
        }
        binding.categoryList.adapter = adapter
        viewModel.allCategories.observe(this.viewLifecycleOwner) {
            it.let {
                categories = it
                adapter.submitList(it)
            }
        }
        binding.categoryList.layoutManager = LinearLayoutManager(this.context)
        binding.addNewWord.setOnClickListener {
            val action = CategoryListFragmentDirections
                .actionCategoryListFragmentToAddNewPairFragment(-1, -1)
            this.findNavController().navigate(action)
        }

        // TODO here
        val itemTouchHelper = ItemTouchHelper(object : SwipeHelper(binding.categoryList) {
            override fun instantiateUnderlayButton(position: Int): List<UnderlayButton> {
                return listOf(deleteButton(position))
            }
        })

        itemTouchHelper.attachToRecyclerView(binding.categoryList)
    }

    private fun deleteButton(position: Int): SwipeHelper.UnderlayButton {
        return SwipeHelper.UnderlayButton(
            requireContext(),
            getString(R.string.button_delete),
            14.0f,
            android.R.color.holo_red_light,
            object : SwipeHelper.UnderlayButtonClickListener {
                override fun onClick() {
                    deleteCategoryDialog(position)

                }
            })
    }

    private fun deleteCategoryDialog(position: Int) {
        val tv = TextView(requireContext())
        tv.text = getString(R.string.category_list_fragment_delete_category_info_message)

        MaterialAlertDialogBuilder(requireContext())
            .setCustomTitle(tv)
            .setNegativeButton(getString(R.string.button_cancel)) { _, _ -> }
            .setPositiveButton(getString(R.string.button_delete)) { dialog, _ ->
                //TODO check out of range
                val categoryName = categories[position].name
                viewModel.deleteCategory(categoryName)
                Toast
                    .makeText(
                        requireContext(),
                        getString(R.string.category_list_fragment_delete_category_complete_message),
                        Toast.LENGTH_SHORT
                    )
                    .show()
            }
            .show()
    }
}