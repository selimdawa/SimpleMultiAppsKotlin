package com.flatcode.simplemultiapps.livetv.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.flatcode.simplemultiapps.livetv.activity.CategoryDetailsActivity
import com.flatcode.simplemultiapps.livetv.model.Category
import com.flatcode.simplemultiapps.utils.intent1
import com.flatcode.simplemultiapps.utils.loadImage
import com.flatcode.simplemultiapps.databinding.ItemLiveTvCategoryBinding

class CategoryAdapter : ListAdapter<Category, CategoryAdapter.ViewHolder>(CategoryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLiveTvCategoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false,
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = getItem(position) ?: return

        with(holder.binding) {
            name.text = model.name
            image.loadImage(model.imageUrl)

            root.setOnClickListener {
                root.context.intent1(CategoryDetailsActivity::class.java) {
                    putExtra("category", model)
                }
            }
        }
    }

    class ViewHolder(val binding: ItemLiveTvCategoryBinding) : RecyclerView.ViewHolder(binding.root)

    class CategoryDiffCallback : DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem == newItem
        }
    }
}