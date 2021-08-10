package com.vaibhav.healthify.ui.homeScreen.dashboardScreen.addWaterDialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.vaibhav.healthify.databinding.FragmentAddWaterDialogBinding
import com.vaibhav.healthify.util.WATER

class AddWaterDialogFragment(private val onAmountSelected: (WATER) -> Unit = {}) :
    BottomSheetDialogFragment() {

    private lateinit var binding: FragmentAddWaterDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddWaterDialogBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAllViews()
    }

    private fun setAllViews() {
        binding.quantity1.setOnClickListener {
            setWaterAndClose(WATER.ML_200)
        }
        binding.quantity2.setOnClickListener {
            setWaterAndClose(WATER.ML_400)
        }
        binding.quantity3.setOnClickListener {
            setWaterAndClose(WATER.ML_600)
        }
        binding.quantity4.setOnClickListener {
            setWaterAndClose(WATER.ML_800)
        }
        binding.quantity5.setOnClickListener {
            setWaterAndClose(WATER.ML_1000)
        }
    }

    private fun setWaterAndClose(water: WATER) {
        onAmountSelected(water)
        dismiss()
    }
}
