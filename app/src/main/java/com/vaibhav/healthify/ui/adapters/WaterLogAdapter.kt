package com.vaibhav.healthify.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vaibhav.healthify.data.models.local.Water
import com.vaibhav.healthify.databinding.WaterListItemBinding

class WaterLogAdapter :
    ListAdapter<Water, WaterLogAdapter.ViewHolder>(DiffCall()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            WaterListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ViewHolder(private val binding: WaterListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Water) {
            binding.water = data
        }
    }

    class DiffCall : DiffUtil.ItemCallback<Water>() {
        override fun areItemsTheSame(
            oldItem: Water,
            newItem: Water
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Water,
            newItem: Water
        ): Boolean {
            return oldItem == newItem
        }
    }
}
