package com.example.mad_exam_3

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory

class Background(screenX: Int, screenY: Int, res: Resources) {

    var x = 0
    var y = 0
    var background: Bitmap

    init {
        val backgroundBitmap = BitmapFactory.decodeResource(res, R.drawable.space2)
        background = Bitmap.createScaledBitmap(backgroundBitmap, screenX, screenY, false)
    }
}