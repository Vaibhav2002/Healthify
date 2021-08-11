package com.vaibhav.healthify.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vaibhav.healthify.databinding.AddWaterListItemBinding
import com.vaibhav.healthify.util.WATER
import com.vaibhav.healthify.util.loadCoilImage
import com.vaibhav.healthify.util.setWaterQuantity

class AddWaterAdapter(
    private val waterList: List<WATER>,
    private val onWaterSelected: (WATER) -> Unit
) :
    RecyclerView.Adapter<AddWaterAdapter.AddWaterVieHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddWaterVieHolder {
        val binding =
            AddWaterListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddWaterVieHolder(binding)
    }

    override fun onBindViewHolder(holder: AddWaterVieHolder, position: Int) {
        holder.bind(waterList[position])
    }

    override fun getItemCount() = waterList.size

    inner class AddWaterVieHolder(private val binding: AddWaterListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                onWaterSelected(waterList[adapterPosition])
            }
        }

        fun bind(water: WATER) {
            binding.waterImage.loadCoilImage(water.image)
            binding.waterQuantity.setWaterQuantity(water.quantity)
        }
    }
}
