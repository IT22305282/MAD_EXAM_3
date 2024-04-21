package com.example.mad_exam_3

import android.graphics.Canvas
import android.graphics.Paint
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import kotlin.concurrent.Volatile

class GameView(activity: GameActivity, screenX: Int, screenY: Int): SurfaceView(activity), Runnable{

    private var thread: Thread? = null
    private val screenX: Int
    private val screenY: Int
    private @Volatile var isPlaying = false
    private @Volatile var isGameOver = false
    private var paint: Paint = Paint()
    private val player: Player
    companion object {
        @JvmStatic
        var screenRatioX = 0f

        @JvmStatic
        var screenRatioY = 0f
    }
    private var background1: Background
    private var background2: Background
    private var surfaceHolder: SurfaceHolder = holder

    init {
        this.screenX = screenX
        this.screenY = screenY

        screenRatioX = MainActivity.GAME_WIDTH.toFloat() / screenX
        screenRatioY = MainActivity.GAME_HEIGHT.toFloat() / screenY

        background1 = Background(screenX, screenY, resources)
        background2 = Background(screenX, screenY, resources)

        background2.x = screenX

        player = Player(this, screenY, resources)
    }

    override fun run() {
        while (isPlaying) {
            val startTime = System.currentTimeMillis()

            update()
            draw()

            val timeTaken = System.currentTimeMillis() - startTime
            val sleepTime = 0.coerceAtLeast((16 - timeTaken).toInt())

            try {
                Thread.sleep(sleepTime.toLong())
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }

    private fun update(){
        background1.x -= (20 * screenRatioX).toInt()
        background2.x -= (20 * screenRatioX).toInt()

        if (background1.x + background1.background.width < 0) {
            background1.x = screenX
        }

        if (background2.x + background2.background.width < 0) {
            background2.x = screenX
        }

        if (player.isGoingUp) {
            player.y -= (25 * screenRatioY).toInt()
        } else {
            player.y += (25 * screenRatioY).toInt()
        }

        if (player.y < 0)
            player.y = 0

        if (player.y >= screenY - player.height)
            player.y = screenY - player.height

    }

    private fun draw(){
        if (surfaceHolder.surface.isValid) {
            val canvas: Canvas? = surfaceHolder.lockCanvas()


            if (canvas != null) {
                canvas.drawBitmap(background1.background, background1.x.toFloat(), background1.y.toFloat(), paint)
                canvas.drawBitmap(background2.background, background2.x.toFloat(), background2.y.toFloat(), paint)

                canvas.drawBitmap(player.getPlayer(), player.x.toFloat(), player.y.toFloat(), paint)

                surfaceHolder.unlockCanvasAndPost(canvas)
            }
        }

    }

    fun resume(){
        isPlaying = true
        thread = Thread(this)
        thread!!.start()
    }

    fun pause(){
        isPlaying = false
        try {
            thread!!.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> if (event.x < screenX / 2) {
                player.isGoingUp = true
            }

            MotionEvent.ACTION_UP -> {
                player.isGoingUp = false
            }
        }
        return true
    }


}
