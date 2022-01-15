package com.example.wificardscanner

import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter

open class GenerateQR() {

    open fun generator(preSharedKey: String, ssid: String, password: String): Bitmap? {
        val textQR = "WIFI:T:${preSharedKey};S:${ssid};P:${password};;"

        val size = 512 //pixels
        // Make the QR code buffer border narrower
        hashMapOf<EncodeHintType, Int>().also { it[EncodeHintType.MARGIN] = 1 }
        val bits = QRCodeWriter().encode(textQR, BarcodeFormat.QR_CODE, size, size)
        val qrImage = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565).also {
            for (x in 0 until size) {
                for (y in 0 until size) {
                    it.setPixel(x, y, if (bits[x, y]) Color.BLACK else Color.WHITE)
                }
            }
        }
        return qrImage
    }
}