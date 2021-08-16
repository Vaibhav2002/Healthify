package com.vaibhav.healthify.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vaibhav.healthify.data.models.OnBoarding
import com.vaibhav.healthify.databinding.OnBoardingLayoutBinding

class OnboardingAdapter(private val onboardingList: List<OnBoarding>) :
    RecyclerView.Adapter<OnboardingAdapter.viewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val binding =
            OnBoardingLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return viewHolder(binding)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val item = onboardingList[position]
        holder.bind(item)
    }

    override fun getItemCount() = onboardingList.size

    inner class viewHolder(private val binding: OnBoardingLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: OnBoarding) {
            binding.onboardingAnim.setAnimation(data.anim)
            binding.onboardingAnim.playAnimation()
        }
    }
}
