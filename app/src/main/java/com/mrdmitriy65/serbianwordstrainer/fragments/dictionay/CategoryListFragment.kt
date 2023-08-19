package com.mrdmitriy65.serbianwordstrainer.fragments.dictionay

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mrdmitriy65.serbianwordstrainer.SerbianWordsTrainerApplication
import com.mrdmitriy65.serbianwordstrainer.adapters.CategoryListAdapter_new
import com.mrdmitriy65.serbianwordstrainer.databinding.FragmentCategoryListBinding
import com.mrdmitriy65.serbianwordstrainer.viewmodels.DictionaryViewModel
import com.mrdmitriy65.serbianwordstrainer.viewmodels.DictionaryViewModelFactory

class CategoryListFragment : Fragment() {
    private var _binding: FragmentCategoryListBinding? = null
    private val binding get() = _binding!!

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

        val adapter = CategoryListAdapter_new{
            val action = CategoryListFragmentDirections
                .actionCategoryListFragmentToWordPairsListFragment(it.id)
            this.findNavController().navigate(action)
        }
        binding.categoryList.adapter = adapter
        viewModel.allCategories.observe(this.viewLifecycleOwner){
            it.let {
                adapter.submitList(it)
            }
        }
        binding.categoryList.layoutManager = LinearLayoutManager(this.context)
        binding.addNewWord.setOnClickListener {
            val action = CategoryListFragmentDirections
                .actionCategoryListFragmentToAddNewPairFragment(-1, -1)
            this.findNavController().navigate(action)
        }
    }
}