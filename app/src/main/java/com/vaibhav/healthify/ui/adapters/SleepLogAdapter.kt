package com.vaibhav.healthify.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vaibhav.healthify.data.models.local.Sleep
import com.vaibhav.healthify.databinding.SleepListItemBinding

class SleepLogAdapter :
    ListAdapter<Sleep, SleepLogAdapter.SleepViewHolder>(DiffCall()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SleepViewHolder {
        val binding =
            SleepListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SleepViewHolder(binding)
    }

    override fun onBindViewHolder(holderSleep: SleepViewHolder, position: Int) {
        val item = getItem(position)
        holderSleep.bind(item)
    }

    inner class SleepViewHolder(private val binding: SleepListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Sleep) {
            binding.sleep = data
        }
    }

    class DiffCall : DiffUtil.ItemCallback<Sleep>() {
        override fun areItemsTheSame(
            oldItem: Sleep,
            newItem: Sleep
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Sleep,
            newItem: Sleep
        ): Boolean {
            return oldItem == newItem
        }
    }
}
