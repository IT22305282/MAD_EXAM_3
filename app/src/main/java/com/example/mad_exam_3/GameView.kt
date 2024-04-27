package com.example.mad_exam_3

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import java.util.Random
import kotlin.concurrent.Volatile

class GameView(activity: GameActivity, screenX: Int, screenY: Int): SurfaceView(activity), Runnable{

    private var thread: Thread? = null
    private val screenX: Int
    private val screenY: Int
    private @Volatile var isPlaying = false
    private @Volatile var isGameOver = false
    private var paint: Paint = Paint()
    private val player: Player
    private lateinit var enemys : Array<Enemy>
    private var score = 0

    companion object {
        @JvmStatic
        var screenRatioX = 0f

        @JvmStatic
        var screenRatioY = 0f
    }

    private var background1: Background
    private var background2: Background
    private var surfaceHolder: SurfaceHolder = holder
    private val random: Random
    private val bullets = mutableListOf<Bullet>()
    private val activity: GameActivity
    private var prefs: SharedPreferences
    val soundPool: SoundPool
    val sound: Int

    init {

        prefs = activity.getSharedPreferences("game", Context.MODE_PRIVATE)

        this.activity = activity
        this.screenX = screenX
        this.screenY = screenY

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_GAME)
                .build()

            soundPool = SoundPool.Builder()
                .setAudioAttributes(audioAttributes)
                .build()
        } else {
            soundPool = SoundPool(1, AudioManager.STREAM_MUSIC, 0)
        }

        sound = soundPool.load(activity, R.raw.shoot, 1)

        screenRatioX = MainActivity.GAME_WIDTH.toFloat() / screenX
        screenRatioY = MainActivity.GAME_HEIGHT.toFloat() / screenY

        background1 = Background(screenX, screenY, resources)
        background2 = Background(screenX, screenY, resources)

        background2.x = screenX

        player = Player(this, screenY, resources)

        paint = Paint()
        paint.textSize = 100f
        paint.color = Color.WHITE

        enemys = Array(3) { Enemy(resources) }

        random = Random()

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
        //background animation content
        background1.x -= (20 * screenRatioX).toInt()
        background2.x -= (20 * screenRatioX).toInt()

        if (background1.x + background1.background.width < 0) {
            background1.x = screenX
        }

        if (background2.x + background2.background.width < 0) {
            background2.x = screenX
        }

        //player movement content
        if (player.isGoingUp) {
            player.y -= (25 * screenRatioY).toInt()
        } else {
            player.y += (25 * screenRatioY).toInt()
        }

        if (player.y < 0)
            player.y = 0

        if (player.y >= screenY - player.height)
            player.y = screenY - player.height

        //player bullet content
        val trash = ArrayList<Bullet>()

        for (bullet in bullets) {
            if (bullet.x > screenX)
                trash.add(bullet)

            bullet.x += (40 * screenRatioX).toInt()

            for (enemy in enemys) {
                if (Rect.intersects(enemy.getCollisionShape(), bullet.getCollisionShape())) {
                    score++
                    enemy.x = -500
                    bullet.x = screenX + 500
                    enemy.wasShot = true
                }
            }
        }

        bullets.removeAll(trash)

        //enemy content
        for(enemy in enemys){
            enemy.x -= (enemy.speed * screenRatioX).toInt()

            if (enemy.x + enemy.width < 0) {
                if (!enemy.wasShot) {
                    isGameOver = true
                    return
                }
                enemy.speed = random.nextInt(10) + 10
                enemy.x = screenX
                enemy.y = random.nextInt(screenY - enemy.height)
                enemy.wasShot = false
            }

            if (Rect.intersects(enemy.getCollisionShape(), player.getCollisionShape())) {
                isGameOver = true
                return
            }
        }
    }

    private fun draw(){
        if (surfaceHolder.surface.isValid) {
            val canvas: Canvas? = surfaceHolder.lockCanvas()

            if (canvas != null) {
                //rendering the background
                canvas.drawBitmap(background1.background, background1.x.toFloat(), background1.y.toFloat(), paint)
                canvas.drawBitmap(background2.background, background2.x.toFloat(), background2.y.toFloat(), paint)

                //rendering the enemies
                for (enemy in enemys) {
                    // Update the position of enemies before drawing them
                    enemy.x -= (enemy.speed * screenRatioX).toInt()
                    canvas.drawBitmap(enemy.getBird(), enemy.x.toFloat(), enemy.y.toFloat(), paint)
                }

                canvas.drawText(score.toString() + "", screenX / 2f, 164f, paint)

                //rendering game over actions
                if (isGameOver) {
                    isPlaying = false
                    //rendering GAME OVER text
                    canvas.drawText("GAME OVER", screenX / 2.5f, screenY / 2f, paint)
                    //rendering explotion animation
                    canvas.drawBitmap(player.getPlayer(), player.x.toFloat(), player.y.toFloat(), paint)
                    surfaceHolder.unlockCanvasAndPost(canvas)
                    saveIfHighScore()
                    waitBeforeExiting()
                    return
                }

                //rendering the player
                canvas.drawBitmap(player.getPlayer(), player.x.toFloat(), player.y.toFloat(), paint)

                //rendering the bullet
                for (bullet in bullets) {
                    canvas.drawBitmap(bullet.getBullet(), bullet.x.toFloat(), bullet.y.toFloat(), paint)
                }

                surfaceHolder.unlockCanvasAndPost(canvas)
            }
        }

    }

    private fun waitBeforeExiting() {
        try {
            Thread.sleep(3000)
            activity.startActivity(Intent(activity, MainActivity::class.java))
            activity.finish()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    private fun saveIfHighScore() {
        if (prefs.getInt("highscore", 0) < score) {
            val editor = prefs.edit()
            editor.putInt("highscore", score)
            editor.apply()
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
        val action = event.actionMasked

        when (action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
                val pointerIndexDown = event.actionIndex
                if (event.getX(pointerIndexDown) < screenX / 2) {
                    player.isGoingUp = true
                } else {
                    player.toShoot++
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> {
                val pointerIndexUp = event.actionIndex
                if (event.getX(pointerIndexUp) < screenX / 2) {
                    player.isGoingUp = false
                }
            }
        }
        return true
    }

    fun newBullet() {

        if (!prefs.getBoolean("isMute", false)) soundPool.play(sound, 1f, 1f, 0, 0, 1f)

        val bullet = Bullet(resources)
        bullet.x = player.x + player.width
        bullet.y = player.y + (player.height/2.5).toInt()
        bullets.add(bullet)
    }

}
