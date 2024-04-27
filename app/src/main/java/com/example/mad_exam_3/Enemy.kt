package com.example.mad_exam_3

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import com.example.mad_exam_3.GameView.Companion.screenRatioX
import com.example.mad_exam_3.GameView.Companion.screenRatioY

class Enemy(res: Resources) {

    var speed = 10
    var wasShot = true
    var x: Int = 0
    var y: Int = 0
    var width: Int
    var height: Int
    var enemyCounter = 1
    var enemy1: Bitmap
    var enemy2: Bitmap
    var enemy3: Bitmap
    var enemy4: Bitmap

    init {
        //enemy assets
        enemy1 = BitmapFactory.decodeResource(res, R.drawable.enemy1)
        enemy2 = BitmapFactory.decodeResource(res, R.drawable.enemy2)
        enemy3 = BitmapFactory.decodeResource(res, R.drawable.enemy3)
        enemy4 = BitmapFactory.decodeResource(res, R.drawable.enemy4)

        width = enemy1.width
        height = enemy1.height

        //increasing the enemy size
        width = (width * 1.5).toInt()
        height = (height * 1.5).toInt()

        width = (width * screenRatioX).toInt()
        height = (height * screenRatioY).toInt()

        enemy1 = Bitmap.createScaledBitmap(enemy1, width, height, false)
        enemy2 = Bitmap.createScaledBitmap(enemy2, width, height, false)
        enemy3 = Bitmap.createScaledBitmap(enemy3, width, height, false)
        enemy4 = Bitmap.createScaledBitmap(enemy4, width, height, false)

        y = -height
    }

    //return Bitmaps to work as an animation
    fun getBird(): Bitmap {
        when (enemyCounter) {
            1 -> {
                enemyCounter++
                return enemy1
            }
            2 -> {
                enemyCounter++
                return enemy2
            }
            3 -> {
                enemyCounter++
                return enemy3
            }
            else -> {
                enemyCounter = 1
                return enemy4
            }
        }
    }

    fun getCollisionShape(): Rect {
        return Rect(x, y, x + width, y + height)
    }

}