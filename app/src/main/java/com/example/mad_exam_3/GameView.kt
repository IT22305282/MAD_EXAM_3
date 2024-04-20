package com.example.mad_exam_3

import android.graphics.Canvas
import android.graphics.Paint
import android.view.SurfaceHolder
import android.view.SurfaceView

class GameView(activity: GameActivity, screenX: Int, screenY: Int): SurfaceView(activity), Runnable{

    private var thread: Thread? = null
    private val screenX: Int
    private val screenY: Int
    private var isPlaying = false
    private var paint: Paint = Paint()
    private var screenRatioX: Float = 0f
    private var screenRatioY: Float = 0f
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
    }

    override fun run() {
        while (isPlaying) {
            val startTime = System.currentTimeMillis()

            update()
            draw()

            val timeTaken = System.currentTimeMillis() - startTime
            val sleepTime = Math.max(0, 16 - timeTaken)

            try {
                Thread.sleep(sleepTime)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }

    private fun update(){
        background1.x -= (10 * screenRatioX).toInt()
        background2.x -= (10 * screenRatioX).toInt()

        if (background1.x + background1.background.width < 0) {
            background1.x = screenX
        }

        if (background2.x + background2.background.width < 0) {
            background2.x = screenX
        }

    }

    private fun draw(){
        if (surfaceHolder.surface.isValid) {
            val canvas: Canvas? = surfaceHolder.lockCanvas()


            if (canvas != null) {
                canvas.drawBitmap(background1.background, background1.x.toFloat(), background1.y.toFloat(), paint)
                canvas.drawBitmap(background2.background, background2.x.toFloat(), background2.y.toFloat(), paint)

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


}
