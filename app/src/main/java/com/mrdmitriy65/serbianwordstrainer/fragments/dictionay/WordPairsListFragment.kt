package com.mrdmitriy65.serbianwordstrainer.fragments.dictionay

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.mrdmitriy65.serbianwordstrainer.SerbianWordsTrainerApplication
import com.mrdmitriy65.serbianwordstrainer.adapters.WordPairListAdapter
import com.mrdmitriy65.serbianwordstrainer.databinding.FragmentWordPairsListBinding
import com.mrdmitriy65.serbianwordstrainer.viewmodels.DictionaryViewModel
import com.mrdmitriy65.serbianwordstrainer.viewmodels.DictionaryViewModelFactory

class WordPairsListFragment : Fragment() {
    private val navigationArgs: WordPairsListFragmentArgs by navArgs()

    private var _binding: FragmentWordPairsListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DictionaryViewModel by activityViewModels {
        DictionaryViewModelFactory(
            (activity?.application as SerbianWordsTrainerApplication)
                .database.wordPairDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWordPairsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = WordPairListAdapter {
            val categoryId = navigationArgs.categoryId
            val action = WordPairsListFragmentDirections
                .actionWordPairsListFragmentToAddNewPairFragment(it.id, categoryId)

            this@WordPairsListFragment.findNavController().navigate(action)
        }
        binding.apply {
            wordPairList.adapter = adapter
            wordPairList.layoutManager = LinearLayoutManager(this@WordPairsListFragment.context)
            addNewWord.setOnClickListener {

                val categoryId = navigationArgs.categoryId
                val action = WordPairsListFragmentDirections
                    .actionWordPairsListFragmentToAddNewPairFragment(-1, categoryId)
                this@WordPairsListFragment.findNavController().navigate(action)
            }
        }
        viewModel.getWordsForCategory(navigationArgs.categoryId)
            .observe(this.viewLifecycleOwner) {
                it.let {
                    adapter.submitList(it)
                }
            }


    }

}