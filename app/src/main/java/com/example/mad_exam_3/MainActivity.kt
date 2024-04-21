package com.example.mad_exam_3

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    companion object {
        @JvmField
        var GAME_WIDTH = 0

        @JvmField
        var GAME_HEIGHT = 0
    }

    private val isMute = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getRealMetrics(dm)

        //setting device width and height
        GAME_WIDTH = dm.widthPixels
        GAME_HEIGHT = dm.heightPixels

        //removing navigation and making the app full screen on the device
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        //letting the app show pass the camera cutout
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }

        //Start button content
        val play = findViewById<ImageView>(R.id.play)

        play.setOnClickListener {
            play.setImageResource(R.drawable.p_start_button_clicked)

            val intent = Intent(this, GameActivity::class.java)

            startActivity(intent)

        }

        //setting highscore
        var highScoreTxt = findViewById<TextView>(R.id.highScoreTxt)

        val prefs = getSharedPreferences("game", MODE_PRIVATE)

        highScoreTxt.setText("HighScore: " + prefs.getInt("highscore", 0))


        //setting up shoot sound
        var isMute = prefs.getBoolean("isMute", false)

        val volumeCtrl = findViewById<ImageView>(R.id.volumeCtrl)

        if (isMute) {
            volumeCtrl.setImageResource(R.drawable.volume_down)
        } else {
            volumeCtrl.setImageResource(R.drawable.volume_up)
        }

        volumeCtrl.setOnClickListener {
            isMute = !isMute
            if (isMute) {
                volumeCtrl.setImageResource(R.drawable.volume_down)
            } else {
                volumeCtrl.setImageResource(R.drawable.volume_up)
            }

            val editor = prefs.edit()
            editor.putBoolean("isMute", isMute)
            editor.apply()
        }

    }
}