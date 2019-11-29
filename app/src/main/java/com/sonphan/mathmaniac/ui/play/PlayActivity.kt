package com.sonphan.mathmaniac.ui.play

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.sonphan.mathmaniac.data.SharedPreferencesConstants
import com.sonphan.mathmaniac.data.local.MathManiacRepositoryImpl
import com.sonphan.mathmaniac.ultility.UserManager
import com.sonphan.user.mathmaniac.R
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_play.*
import java.util.*

class PlayActivity : AppCompatActivity() {
    private var playSuccess: MediaPlayer? = null
    private var playGameOver: MediaPlayer? = null
    private var timer: CountDownTimer? = null
    private var isLose: Boolean = false
    internal var isHighScore: Boolean = false
    private var vNumA: Int = 0
    private var vNumB: Int = 0
    private var vResult: Int = 0
    internal var vScore: Int = 0
    private var pivot: Int = 0
    internal var vHighScore: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)
        addControls()
        addEvents()
    }

    override fun onResume() {
        super.onResume()
        //Get high score
        val pref = getSharedPreferences(SharedPreferencesConstants.HIGH_SCORE_NAME, Context.MODE_PRIVATE)
        vHighScore = pref.getInt(SharedPreferencesConstants.HIGH_SCORE_KEY, 0)
        highScore.text = vHighScore.toString()

        playSuccess = MediaPlayer.create(applicationContext, R.raw.success)
        playGameOver = MediaPlayer.create(applicationContext, R.raw.game_over)
    }

    private fun addEvents() {
        btnYes.setOnClickListener {
            if (isLose) {
                return@setOnClickListener
            }
            if (vNumA + vNumB == vResult) {
                playSuccess?.start()
                if (vScore != 0) {
                    timer?.cancel()
                }
                vScore += 1
                if (vScore > Integer.parseInt(highScore.text.toString())) {
                    vHighScore = vScore
                    highScore.text = vScore.toString()
                    isHighScore = true
                }
                score.text = vScore.toString()
                gameActivity()
                return@setOnClickListener
            }
            lose(isHighScore)
        }

        btnNo.setOnClickListener {
            if (isLose) {
                return@setOnClickListener
            }
            if (vNumA + vNumB != vResult) {
                playSuccess?.start()
                if (vScore != 0) {
                    timer?.cancel()
                }
                vScore += 1
                if (vScore > Integer.parseInt(highScore.text.toString())) {
                    vHighScore = vScore
                    highScore.text = vScore.toString()
                    isHighScore = true
                }
                score.text = vScore.toString()
                gameActivity()
                return@setOnClickListener
            }
            lose(isHighScore)
        }

        continuePlay.setOnClickListener {
            val intent = Intent(applicationContext, PlayActivity::class.java)
            startActivity(intent)
            finish()
        }

        menu.setOnClickListener {
            finish()
        }
    }

    private fun addControls() {
        time.progress = 100
        vScore = 0
        isLose = false
        isHighScore = false
        gameActivity()
    }

    private fun gameActivity() {
        val random = Random()
        if (vScore <= 10) {
            vNumA = random.nextInt(7) + 1
            vNumB = random.nextInt(7) + 1
        } else {
            vNumA = random.nextInt(15) + 1
            vNumB = random.nextInt(15) + 1
        }
        pivot = random.nextInt(2)
        if (pivot == 0) {
            pivot = random.nextInt(2)
            if (pivot == 0) {
                vResult = vNumA + vNumB + random.nextInt(5) + 1
            } else {
                do {
                    vResult = vNumA + vNumB - random.nextInt(5) - 1
                } while (vResult <= 0)
            }
        } else {
            vResult = vNumA + vNumB
        }
        numA.text = vNumA.toString()
        numB.text = vNumB.toString()
        result.text = vResult.toString()
        if (vScore != 0) {
            mainLayout.startAnimation(AnimationUtils.loadAnimation(applicationContext, R.anim.slide_from_right_to_left))
            timer = object : CountDownTimer(1000, 10) {
                override fun onTick(l: Long) {
                    val percent = l / 1000.0 * 100
                    time.progress = percent.toInt()
                }

                override fun onFinish() {
                    lose(isHighScore)
                }
            }.start()
        }

    }

    override fun onPause() {
        super.onPause()
        saveHighScore()
        playSuccess?.release()
        playGameOver?.release()
    }

    private fun saveHighScore() {
        val pref = getSharedPreferences("highScore", Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putInt("highScore", vHighScore)
        editor.apply()

        if (UserManager.hasUser()) {
            UserManager.user?.fbId?.let {
                MathManiacRepositoryImpl.instance.setHighScore(
                        it,
                        vHighScore
                )
                        .subscribeOn(Schedulers.io())
                        .subscribeBy(
                                onError = { e -> Log.w(this::class.java.toString(), e.message) }
                        )
            }
        }
    }

    private fun lose(isHighScore: Boolean) {
        timer?.cancel()
        playGameOver?.start()
        isLose = true
        val anim = AnimationUtils.loadAnimation(applicationContext, R.anim.shake)
        activityPlay.startAnimation(anim)
        anim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {

            }

            override fun onAnimationEnd(animation: Animation) {
                gameOver.visibility = View.VISIBLE
                gameOver.startAnimation(AnimationUtils.loadAnimation(applicationContext, R.anim.slide_from_above))
                resultYourScore.text = vScore.toString()
                resultHighScore.text = vHighScore.toString()
                if (isHighScore) {
                    resultHighScore.setTextColor(Color.YELLOW)
                }
            }

            override fun onAnimationRepeat(animation: Animation) {

            }
        })
    }

    override fun onBackPressed() {
        if (isLose) {
            super.onBackPressed()
        }
        //else do nothing
    }
}
