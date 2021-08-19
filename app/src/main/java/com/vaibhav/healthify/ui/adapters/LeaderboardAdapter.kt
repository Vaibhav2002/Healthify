package com.vaibhav.healthify.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vaibhav.healthify.data.models.local.LeaderBoardItem
import com.vaibhav.healthify.databinding.LeaderboardRvItemBinding

class LeaderboardAdapter :
    ListAdapter<LeaderBoardItem, LeaderboardAdapter.viewHolder>(DiffCall()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val binding =
            LeaderboardRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return viewHolder(binding)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class viewHolder(private val binding: LeaderboardRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: LeaderBoardItem) {
            binding.leaderBoardItem = data
        }
    }

    class DiffCall : DiffUtil.ItemCallback<LeaderBoardItem>() {
        override fun areItemsTheSame(
            oldItem: LeaderBoardItem,
            newItem: LeaderBoardItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: LeaderBoardItem,
            newItem: LeaderBoardItem
        ): Boolean {
            return oldItem == newItem
        }
    }
}
