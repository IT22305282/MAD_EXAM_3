package com.example.mad_exam_3

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import com.example.mad_exam_3.GameView.Companion.screenRatioX
import com.example.mad_exam_3.GameView.Companion.screenRatioY

class Bullet (res: Resources) {

    var width: Int
    var height: Int
    var x: Int = 0
    var y: Int = 0
    var bullet1: Bitmap
    var bullet2: Bitmap
    var bullet3: Bitmap
    var bullet4: Bitmap
    var bulletCounter = 0

    init {
        bullet1 = BitmapFactory.decodeResource(res, R.drawable.shot1)
        bullet2 = BitmapFactory.decodeResource(res, R.drawable.shot2)
        bullet3 = BitmapFactory.decodeResource(res, R.drawable.shot3)
        bullet4 = BitmapFactory.decodeResource(res, R.drawable.shot4)

        width = bullet1.width
        height = bullet1.height

        width = (width * screenRatioX).toInt()
        height = (height * screenRatioY).toInt()

        bullet1 = Bitmap.createScaledBitmap(bullet1, width, height,false)
        bullet2 = Bitmap.createScaledBitmap(bullet2, width, height,false)
        bullet3 = Bitmap.createScaledBitmap(bullet3, width, height,false)
        bullet4 = Bitmap.createScaledBitmap(bullet4, width, height,false)
    }

    fun getBullet() : Bitmap{
        when(bulletCounter){
            1 -> {
                bulletCounter++
                return bullet1
            }

            2 -> {
                bulletCounter++
                return bullet2
            }

            3 -> {
                bulletCounter++
                return bullet3
            }

            else -> {
                bulletCounter = 1
                return bullet4
            }
        }
    }

    fun getCollisionShape(): Rect {
        return Rect(x, y, x + width, y + height)
    }

}