package com.example.mad_exam_3

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import com.example.mad_exam_3.GameView.Companion.screenRatioX
import com.example.mad_exam_3.GameView.Companion.screenRatioY

class Player(gameView: GameView, screenY: Int, res: Resources) {

    private var gameView: GameView
    var player1: Bitmap
    var player2: Bitmap
    var player3: Bitmap
    var player4: Bitmap
    var width: Int
    var height: Int
    var x: Int = 0
    var y: Int = 0
    var playerCounter = 0
    var isGoingUp = false

    init {
        this.gameView = gameView

        player1 = BitmapFactory.decodeResource(res, R.drawable.player1)
        player2 = BitmapFactory.decodeResource(res, R.drawable.player2)
        player3 = BitmapFactory.decodeResource(res, R.drawable.player3)
        player4 = BitmapFactory.decodeResource(res, R.drawable.player4)

        width = player1.width
        height = player1.height

        width = (width * screenRatioX).toInt()
        height = (height * screenRatioY).toInt()

        player1 = Bitmap.createScaledBitmap(player1, width, height, false)
        player2 = Bitmap.createScaledBitmap(player2, width, height, false)
        player3 = Bitmap.createScaledBitmap(player3, width, height, false)
        player4 = Bitmap.createScaledBitmap(player4, width, height, false)

        y = screenY / 2
        x = (74 * screenRatioX).toInt()

    }

    fun getPlayer(): Bitmap{
        val currentFrame = when (playerCounter) {
            0 -> player1
            1 -> player2
            2 -> player3
            else -> player4
        }

        playerCounter = (playerCounter + 1) % 4 // Loop back to the first frame after the last frame

        return currentFrame
    }

    fun getCollisionShape(): Rect {
        return Rect(x, y, x + width, y + height)
    }


}