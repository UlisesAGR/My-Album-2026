/*
 * CustomToolbar
 * Copyright © 2026. All rights reserved
 */
package com.myalbum2026.mobile.presenter.widget.toolbar

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.text.HtmlCompat
import com.myalbum2026.mobile.R
import com.myalbum2026.mobile.databinding.ToolbarBinding
import com.myalbum2026.mobile.utils.ui.setOnSafeClickListener

class CustomToolbar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ToolbarBinding.inflate(LayoutInflater.from(context), this, true)
    }

    init {
        binding
    }

    fun setTitle(title: String?) {
        if (!title.isNullOrEmpty()) {
            val titleShort = HtmlCompat.fromHtml(title, HtmlCompat.FROM_HTML_MODE_LEGACY)
            binding.titleToolbarCustomTextView.setTextWithEllipsis(titleShort.toString())
        }
    }

    fun setTitleTextSize(textSize: Float) {
        binding.titleToolbarCustomTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize)
    }

    fun setIconLeft(icon: Int?) {
        icon?.let {
            binding.leftImageView.setImageResource(icon)
            if (icon == R.drawable.ic_arrow_back) {
                binding.leftImageView.contentDescription = context.getString(R.string.back)
            }
        }
    }

    fun setTitleAlignment(alignment: Int) {
        binding.titleToolbarCustomTextView.gravity = when (alignment) {
            Gravity.START -> {
                Gravity.START
            }
            Gravity.CENTER -> {
                Gravity.CENTER_HORIZONTAL
            }
            Gravity.END -> {
                Gravity.END
            }
            else -> {
                Gravity.CENTER_HORIZONTAL
            }
        }
    }

    fun setOnIconLeftClickListener(listener: () -> Unit) {
        binding.leftImageView.setOnSafeClickListener {
            listener()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun TextView.setTextWithEllipsis(fullText: String) {
        val availableWidth = this.width - this.paddingStart - this.paddingEnd
        if (availableWidth <= 0) {
            post { setTextWithEllipsis(fullText) }
            return
        }
        val paint = this.paint
        val ellipsis = "..."
        val ellipsisWidth = paint.measureText(ellipsis)
        if (paint.measureText(fullText) <= availableWidth) {
            this.text = fullText
            return
        }
        var cutIndex = fullText.length
        while (cutIndex > 0 && paint.measureText(fullText, 0, cutIndex) + ellipsisWidth > availableWidth) {
            cutIndex--
        }
        this.text = fullText.take(cutIndex) + ellipsis
    }
}
