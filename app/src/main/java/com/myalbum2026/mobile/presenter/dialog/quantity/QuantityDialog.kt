/*
 * QuantityBottomSheet
 * Copyright © 2026. All rights reserved
 */
package com.myalbum2026.mobile.presenter.dialog.quantity

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.myalbum2026.mobile.R
import com.myalbum2026.mobile.databinding.QuantityDialogBinding
import com.myalbum2026.mobile.utils.binding.viewBinding
import com.myalbum2026.mobile.utils.extensions.Constants.DELAY_QUANTITY
import com.myalbum2026.mobile.utils.extensions.intToString
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class QuantityBottomSheet(
    private val initialQuantity: Int,
    private val title: String,
    private val onConfirm: (Int) -> Unit,
) : DialogFragment(R.layout.quantity_dialog) {

    private val binding by viewBinding(QuantityDialogBinding::bind)

    private var count = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        count = initialQuantity
        setupUI()
        setupListeners()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomDialogStyle)
    }

    private fun setupUI() = with(binding) {
        titleTxt.text = title
        quantityEdt.setText(count.intToString())
        updateValidator()
    }

    private fun setupListeners() = with(binding) {
        imgPlus.setOnClickListener {
            plusEvent()
        }

        imgLess.setOnClickListener {
            minusEvent()
        }

        imgPlus.setOnLongClickListener {
            onLongEvent(Operators.PLUS)
            true
        }

        imgLess.setOnLongClickListener {
            onLongEvent(Operators.MINUS)
            true
        }

        acceptBtn.setOnClickListener {
            onConfirm(count)
            dismiss()
        }

        cancelBtn.setOnClickListener {
            dismiss()
        }
    }

    private fun onLongEvent(operator: Operators) {
        viewLifecycleOwner.lifecycleScope.launch {
            delay(DELAY_QUANTITY)
            when (operator) {
                Operators.PLUS -> {
                    if (binding.imgPlus.isPressed) {
                        plusEvent()
                        onLongEvent(operator)
                    }
                }
                Operators.MINUS -> {
                    if (count > 0 && binding.imgLess.isPressed) {
                        minusEvent()
                        onLongEvent(operator)
                    }
                }
            }
        }
    }

    private fun plusEvent() {
        count++
        renderCount()
    }

    private fun minusEvent() {
        if (count > 0) {
            count--
            renderCount()
        }
    }

    private fun renderCount() {
        binding.quantityEdt.setText(count.intToString())
        updateValidator()
    }

    private fun updateValidator() {
        binding.imgLess.isEnabled = count > 0
    }

    enum class Operators {
        PLUS,
        MINUS,
    }

    companion object {
        const val TAG = "QuantityBottomSheet"
    }
}
