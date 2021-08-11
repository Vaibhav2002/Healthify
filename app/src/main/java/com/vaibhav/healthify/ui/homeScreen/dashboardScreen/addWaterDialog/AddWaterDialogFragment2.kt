package com.vaibhav.healthify.ui.homeScreen.dashboardScreen.addWaterDialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.vaibhav.healthify.databinding.FragmentAddWaterDialog2Binding
import com.vaibhav.healthify.ui.adapters.AddWaterAdapter
import com.vaibhav.healthify.util.WATER
import com.vaibhav.healthify.util.waterList

class AddWaterDialogFragment2(private val onAmountSelected: (WATER) -> Unit = {}) :
    DialogFragment() {

    private lateinit var binding: FragmentAddWaterDialog2Binding
    private val waterAdapter = AddWaterAdapter(waterList) {
//        onAmountSelected(it)
//        dismiss()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddWaterDialog2Binding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addWaterRv.apply {
            setHasFixedSize(true)
            adapter = waterAdapter
        }
    }

    private fun setWaterAndClose(water: WATER) {
        onAmountSelected(water)
        dismiss()
    }
}
