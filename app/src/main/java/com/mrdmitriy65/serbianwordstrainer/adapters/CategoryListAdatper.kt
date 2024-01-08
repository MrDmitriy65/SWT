package com.mrdmitriy65.serbianwordstrainer.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chauthai.swipereveallayout.SwipeRevealLayout
import com.mrdmitriy65.serbianwordstrainer.R
import com.mrdmitriy65.serbianwordstrainer.databinding.ItemCategorySwipeBinding
import com.mrdmitriy65.serbianwordstrainer.data.entities.Category
import com.chauthai.swipereveallayout.ViewBinderHelper;

class CategoryListAdapter(private val onItemClicked: (Category) -> Unit) :
    ListAdapter<Category, CategoryListAdapter.CategoryViewHolder>(DiffCallback) {
        val bindHelper = ViewBinderHelper()
    class CategoryViewHolder(private val binding: ItemCategorySwipeBinding) :
        RecyclerView.ViewHolder(binding.root) {
            val layout: SwipeRevealLayout = binding.swipeLayout
            val text: TextView = binding.categoryName

        fun bind(category: Category) {
            binding.apply {
                categoryName.text = category.name
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            ItemCategorySwipeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        var current = getItem(position)
        holder.text.setOnClickListener {
            onItemClicked(current)
        }
        bindHelper.bind(holder.layout, current.name)
        holder.bind(current)
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Category>() {
            override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
                return oldItem.id == newItem.id && oldItem.name == newItem.name
            }

        }
    }
}