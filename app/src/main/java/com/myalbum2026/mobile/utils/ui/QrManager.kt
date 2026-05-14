/*
 * QrManager
 * Copyright © 2026. All rights reserved
 */
package com.myalbum2026.mobile.utils.ui

import android.graphics.Bitmap
import android.widget.ImageView
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.myalbum2026.mobile.utils.logger.log

fun ImageView.setQrCode(
    data: String,
    size: Int = 800,
) {
    try {
        val barcodeEncoder = BarcodeEncoder()
        val bitmap: Bitmap = barcodeEncoder.encodeBitmap(
            data,
            BarcodeFormat.QR_CODE,
            size,
            size,
        )
        this.setImageBitmap(bitmap)
    } catch (exception: Exception) {
        log(message = exception.message.toString())
    }
}

inline fun DecoratedBarcodeView.setupScanner(crossinline onResult: (String) -> Unit) {
    this.decodeContinuous(object : BarcodeCallback {
        override fun barcodeResult(result: BarcodeResult?) {
            result?.text?.let { qrData ->
                this@setupScanner.pause()
                onResult(qrData)
            }
        }
        override fun possibleResultPoints(resultPoints: MutableList<com.google.zxing.ResultPoint>?) {}
    })
}
