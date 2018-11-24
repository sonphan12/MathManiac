package com.example.user.mathmaniac.ui.menu

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.user.mathmaniac.R
import com.example.user.mathmaniac.ui.play.PlayActivity
import kotlinx.android.synthetic.main.activity_main_menu.*

class MainMenuActivity : AppCompatActivity(), IMainMenuView {
    private lateinit var mPresenter: MainMenuPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)
        mPresenter = MainMenuPresenter()
        btnPlay.setOnClickListener { mPresenter.onPlayClicked() }
    }

    override fun onResume() {
        super.onResume()
        mPresenter.attachView(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }

    override fun navigateToPlay() {
        val intent = Intent(this@MainMenuActivity, PlayActivity::class.java)
        startActivity(intent)
    }
}
