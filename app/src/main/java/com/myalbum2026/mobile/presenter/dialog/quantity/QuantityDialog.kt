/*
 * QuantityDialog.kt
 * Copyright (c) 2026. All rights reserved
 */
package com.myalbum2026.mobile.presenter.dialog.quantity

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.myalbum2026.mobile.databinding.QuantityDialogBinding
import com.myalbum2026.mobile.utils.extensions.Constants.DELAY_QUANTITY
import com.myalbum2026.mobile.utils.extensions.getInt
import com.myalbum2026.mobile.utils.extensions.intToString
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class QuantityDialog(
    context: Context,
    private val onClickListener: (quantity: Int) -> Unit,
) {

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        QuantityDialogBinding.inflate(LayoutInflater.from(context))
    }

    private var dialog: AlertDialog
    private var count = 0

    init {
        val builder = AlertDialog.Builder(context)
        builder.setView(binding.root)
        dialog = builder.create()
        setListener()
    }

    private fun setListener() = with(binding) {
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
        cancelBtn.setOnClickListener {
            hide()
        }
        acceptBtn.setOnClickListener {
            onClickListener(quantityEdt.getInt())
            hide()
        }
    }

    private fun onLongEvent(value: Operators) {
        dialog.lifecycleScope.launch {
            delay(DELAY_QUANTITY)
            when (value) {
                Operators.PLUS -> {
                    if (binding.imgPlus.isPressed) {
                        plusEvent()
                        onLongEvent(value)
                    }
                }

                Operators.MINUS -> {
                    if (count > 0) {
                        if (binding.imgLess.isPressed) {
                            minusEvent()
                            onLongEvent(value)
                        }
                    }
                }
            }
        }
    }

    private fun plusEvent() {
        count += 1
        validator()
        binding.quantityEdt.setText(count.intToString())
    }

    private fun minusEvent() = with(binding) {
        count -= 1
        validator()
        quantityEdt.setText(count.intToString())
    }

    private fun validator() = with(binding) {
        imgLess.isEnabled = when {
            count > 0 -> true
            else -> false
        }
    }

    fun setTitleCustom(title: String) = with(binding) {
        titleTxt.text = title
    }

    fun setValue(quantity: Int) = with(binding) {
        count = quantity
        validator()
        quantityEdt.setText(quantity.intToString())
    }

    fun show() = dialog.show()

    private fun hide() = dialog.dismiss()

    fun setCancelable(isCancelable: Boolean) =
        dialog.setCancelable(isCancelable)

    enum class Operators {
        PLUS,
        MINUS,
    }
}
