package com.example.user.mathmaniac.ui

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.user.mathmaniac.R
import kotlinx.android.synthetic.main.activity_main_menu.*

class MainMenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        btnPlay.setOnClickListener {
            val intent = Intent(this@MainMenuActivity, PlayActivity::class.java)
            startActivity(intent)
        }
    }

}
