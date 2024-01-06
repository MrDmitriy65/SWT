package com.mrdmitriy65.serbianwordstrainer.fragments.dictionay

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import com.mrdmitriy65.serbianwordstrainer.R
import com.mrdmitriy65.serbianwordstrainer.SerbianWordsTrainerApplication
import com.mrdmitriy65.serbianwordstrainer.adapters.WordPairListAdapter
import com.mrdmitriy65.serbianwordstrainer.data.entities.WordPair
import com.mrdmitriy65.serbianwordstrainer.databinding.FragmentWordPairsListBinding
import com.mrdmitriy65.serbianwordstrainer.helpers.SwipeHelper
import com.mrdmitriy65.serbianwordstrainer.viewmodels.DictionaryViewModel
import com.mrdmitriy65.serbianwordstrainer.viewmodels.DictionaryViewModelFactory

class WordPairsListFragment : Fragment() {
    private val navigationArgs: WordPairsListFragmentArgs by navArgs()

    private var _binding: FragmentWordPairsListBinding? = null
    private val binding get() = _binding!!
    private lateinit var pairs : List<WordPair>

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
                pairs = it
                it.let {
                    adapter.submitList(it)
                }
            }

        val itemTouchHelper = ItemTouchHelper(object : SwipeHelper(binding.wordPairList) {
            override fun instantiateUnderlayButton(position: Int): List<UnderlayButton> {
                return listOf(deleteButton(position))
            }
        })

        itemTouchHelper.attachToRecyclerView(binding.wordPairList)
    }

    private fun deleteButton(position: Int) : SwipeHelper.UnderlayButton {
        return SwipeHelper.UnderlayButton(
            requireContext(),
            getString(R.string.button_delete),
            14.0f,
            android.R.color.holo_red_light,
            object : SwipeHelper.UnderlayButtonClickListener {
                override fun onClick() {
                    //TODO check out of range
                    val pair = pairs[position]
                    viewModel.deletePair(pair.russian, pair.serbian)
                    Toast
                        .makeText(requireContext(), getText(R.string.word_pairs_list_fragment_delete_complete), Toast.LENGTH_SHORT)
                        .show()
                }
            })
    }

}