package com.vaibhav.healthify.ui.dialogs.editWaterLimitDialog

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.vaibhav.healthify.databinding.FragmentEditWaterLimitBinding
import com.warkiz.widget.IndicatorSeekBar
import com.warkiz.widget.OnSeekChangeListener
import com.warkiz.widget.SeekParams
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class EditWaterLimitFragment(
    private val onLimitSelected: (Int) -> Unit = {},
    private val onDismiss: () -> Unit = {}
) :
    BottomSheetDialogFragment() {

    private lateinit var binding: FragmentEditWaterLimitBinding
    private val viewModel by viewModels<EditWaterLimitViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditWaterLimitBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.waterQuantitySeekBar.onSeekChangeListener = object : OnSeekChangeListener {
            override fun onSeeking(seekParams: SeekParams?) {
                seekParams?.let { viewModel.onQuantityChange(it.progress) }
            }

            override fun onStartTrackingTouch(seekBar: IndicatorSeekBar?) = Unit

            override fun onStopTrackingTouch(seekBar: IndicatorSeekBar?) = Unit
        }

        binding.btnSave.setOnClickListener {
            viewModel.onSaveButtonPressed()
        }

        collectUiState()
        collectUIEvents()
    }

    private fun collectUIEvents() = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
        viewModel.events.collect {
            when (it) {
                is EditWaterDialogEvents.NavigateBack -> navigateBack(it.quantity)
            }
        }
    }

    private fun collectUiState() = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
        viewModel.uiState.collect {
            binding.waterQuantity.text = "${it.quantitySelected} mL"
            binding.btnSave.isEnabled = it.isButtonEnabled
        }
    }

    private fun navigateBack(quantity: Int) {
        onLimitSelected(quantity)
        dismiss()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismiss()
    }
}
