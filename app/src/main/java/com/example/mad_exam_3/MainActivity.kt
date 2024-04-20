package com.example.mad_exam_3

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.widget.ImageView

class MainActivity : AppCompatActivity() {

    companion object {
        @JvmField
        var GAME_WIDTH = 0

        @JvmField
        var GAME_HEIGHT = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getRealMetrics(dm)

        GAME_WIDTH = dm.widthPixels
        GAME_HEIGHT = dm.heightPixels

        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }

        val play = findViewById<ImageView>(R.id.play)

        play.setOnClickListener {
            play.setImageResource(R.drawable.p_start_button_clicked)

            val intent = Intent(this, GameActivity::class.java)

            startActivity(intent)

        }





    }
}