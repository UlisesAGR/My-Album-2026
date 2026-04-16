/*
 * CustomButton
 * Copyright © 2026. All rights reserved
 */
package com.myalbum2026.mobile.presenter.widget.button

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.Gravity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.content.withStyledAttributes
import com.myalbum2026.mobile.presenter.widget.button.CustomButtonType.BTN_DISABLED
import com.myalbum2026.mobile.presenter.widget.button.CustomButtonType.BTN_PRIMARY
import com.myalbum2026.mobile.presenter.widget.button.CustomButtonType.BTN_SECONDARY
import com.myalbum2026.mobile.presenter.widget.button.CustomButtonType.BTN_WITHOUT_BORDERS_PRIMARY
import com.myalbum2026.mobile.presenter.widget.button.CustomButtonType.BTN_WITHOUT_BORDERS_SECONDARY
import com.myalbum2026.mobile.utils.extensions.Constants.BTN_NORMAL_DEFAULT_HEIGHT
import com.myalbum2026.mobile.utils.extensions.Constants.BTN_WITHOUT_BORDERS_DEFAULT_HEIGHT
import com.google.android.material.button.MaterialButton
import com.myalbum2026.mobile.R

class CustomButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : MaterialButton(context, attrs, defStyleAttr) {

    private var btnType: Int = BTN_DISABLED
    private var styleApplied: Boolean = false
    private var originalBtnType: Int = BTN_DISABLED

    init {
        if (attrs != null) {
            context.withStyledAttributes(attrs, R.styleable.CustomButton) {
                btnType = getInt(R.styleable.CustomButton_btnType, BTN_DISABLED)
                originalBtnType = btnType
            }
        }

        typeface = ResourcesCompat.getFont(context, R.font.amx_medium)
        letterSpacing = 0.01f
        gravity = Gravity.CENTER
        cornerRadius = 70
        textSize = 16f
        includeFontPadding = false

        if (!isEnabled) {
            applyButtonStyle(BTN_DISABLED)
        } else {
            applyButtonStyle(btnType)
            styleApplied = true
        }
    }

    fun applyButtonStyle(btnType: Int) {
        when (btnType) {
            BTN_PRIMARY -> {
                setBackgroundColor(ContextCompat.getColor(context, R.color.md_theme_primary))
                setTextColor(ContextCompat.getColor(context, R.color.text_white_color))
                height = dpToPx(BTN_NORMAL_DEFAULT_HEIGHT)
            }
            BTN_SECONDARY -> {
                setBackgroundColor(ContextCompat.getColor(context, R.color.md_theme_secondary))
                setTextColor(ContextCompat.getColor(context, R.color.text_white_color))
                height = dpToPx(BTN_NORMAL_DEFAULT_HEIGHT)
            }
            BTN_WITHOUT_BORDERS_PRIMARY -> {
                setStrokeBackground(
                    strokeColor = ContextCompat.getColor(context, R.color.md_theme_primary),
                    fillColor = ContextCompat.getColor(context, R.color.transparent),
                )
                setTextColor(ContextCompat.getColor(context, R.color.md_theme_primary))
                height = dpToPx(BTN_WITHOUT_BORDERS_DEFAULT_HEIGHT)
            }
            BTN_WITHOUT_BORDERS_SECONDARY -> {
                setStrokeBackground(
                    strokeColor = ContextCompat.getColor(context, R.color.md_theme_secondary),
                    fillColor = ContextCompat.getColor(context, R.color.transparent),
                )
                setTextColor(ContextCompat.getColor(context, R.color.md_theme_secondary))
                height = dpToPx(BTN_WITHOUT_BORDERS_DEFAULT_HEIGHT)
            }
            BTN_DISABLED -> {
                setBackgroundColor(ContextCompat.getColor(context, R.color.button_enable))
                setTextColor(ContextCompat.getColor(context, R.color.button_text))
                height = dpToPx(BTN_NORMAL_DEFAULT_HEIGHT)
            }
        }
    }

    private fun dpToPx(dp: Int): Int =
        (dp * resources.displayMetrics.density).toInt()

    private fun dpToPxFloat(): Float =
        50f * resources.displayMetrics.density

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        if (styleApplied) {
            applyButtonStyle(btnType = if (enabled) btnType else BTN_DISABLED)
        }
    }

    private fun setStrokeBackground(
        strokeColor: Int,
        fillColor: Int = Color.TRANSPARENT,
    ) {
        val drawable = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            setColor(fillColor)
            setStroke(dpToPx(dp = 1), strokeColor)
            cornerRadius = dpToPxFloat()
        }
        rippleColor = null
        backgroundTintList = null
        insetTop = 0
        insetBottom = 0
        background = drawable
    }
}
