package com.mrdmitriy65.serbianwordstrainer.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mrdmitriy65.serbianwordstrainer.databinding.WordTranslatePairBinding
import com.mrdmitriy65.serbianwordstrainer.models.TranslatePair

class WordTranslatePairAdapter() :
    ListAdapter<TranslatePair, WordTranslatePairAdapter.WordTranslatePairViewHolder>(
        DiffCallback
    ) {

    private lateinit var context: Context

    class WordTranslatePairViewHolder(val binding: WordTranslatePairBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(pair: TranslatePair) {
            binding.wordText.text = pair.word
            binding.translateText.text = pair.translate
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordTranslatePairViewHolder {
        context = parent.context
        return WordTranslatePairViewHolder(
            WordTranslatePairBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: WordTranslatePairViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<TranslatePair>() {
            override fun areItemsTheSame(oldItem: TranslatePair, newItem: TranslatePair): Boolean {
                return (oldItem.translate == newItem.translate &&
                        oldItem.word == newItem.word
                        )
            }

            override fun areContentsTheSame(
                oldItem: TranslatePair,
                newItem: TranslatePair
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}