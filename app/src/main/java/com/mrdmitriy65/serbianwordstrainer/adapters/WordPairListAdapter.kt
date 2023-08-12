package com.mrdmitriy65.serbianwordstrainer.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mrdmitriy65.serbianwordstrainer.data.entities.WordPair
import com.mrdmitriy65.serbianwordstrainer.databinding.ItemWordPairBinding

class WordPairListAdapter(private val onItemClicked: (WordPair) -> Unit):
    ListAdapter<WordPair, WordPairListAdapter.WordPairListViewHolder>(DiffCallback) {

    class WordPairListViewHolder(private val binding: ItemWordPairBinding):
        RecyclerView.ViewHolder(binding.root) {
            fun bind(wordPair: WordPair){
                binding.russianWord.text = wordPair.russian
                binding.serbianWord.text = wordPair.serbian
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordPairListViewHolder {
        return WordPairListViewHolder(
            ItemWordPairBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: WordPairListViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
        holder.itemView.setOnClickListener{onItemClicked(current)}
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<WordPair>(){
            override fun areItemsTheSame(oldItem: WordPair, newItem: WordPair): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: WordPair, newItem: WordPair): Boolean {
                return oldItem.id == newItem.id
                        && oldItem.russian == newItem.russian
                        && oldItem.serbian == newItem.serbian
            }

        }
    }
}