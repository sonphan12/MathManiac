package com.sonphan.mathmaniac.ui.menu

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.example.user.mathmaniac.R
import com.facebook.*
import com.facebook.login.LoginResult
import com.sonphan.mathmaniac.AndroidApplication
import com.sonphan.mathmaniac.data.FacebookPermissionConstants
import com.sonphan.mathmaniac.data.local.MathManiacRepositoryImpl
import com.sonphan.mathmaniac.data.model.Player
import com.sonphan.mathmaniac.ui.play.PlayActivity
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_main_menu.*


class MainMenuActivity : AppCompatActivity(), IMainMenuView {
    private lateinit var mPresenter: MainMenuPresenter
    private val mCallBackManager = CallbackManager.Factory.create()
    private lateinit var mLeaderBoardDialog: LeaderBoardDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)
        mLeaderBoardDialog = LeaderBoardDialog(this)
        mPresenter = MainMenuPresenter(
                MathManiacRepositoryImpl(
                        (this.application as AndroidApplication).playerDao,
                        (this.application as AndroidApplication).mfacebookFriendDao,
                        this.applicationContext
                )
        )
        btnPlay.setOnClickListener { mPresenter.onPlayClicked() }
        btnRank.setOnClickListener { mPresenter.onRankClicked() }
        initLoginFacebook()
    }

    private fun initLoginFacebook() {
        fbLoginButton.setReadPermissions(FacebookPermissionConstants.USER_FRIENDS)
        fbLoginButton.registerCallback(mCallBackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                val profile = Profile.getCurrentProfile()
                mPresenter.onLoginSuccess(profile.id, profile.name, profile.getProfilePictureUri(60, 60), AccessToken.getCurrentAccessToken())
            }

            override fun onCancel() {

            }

            override fun onError(error: FacebookException?) {
                mPresenter.onLoginFailed()
            }
        })

        btnLogin.setOnClickListener { fbLoginButton.performClick() }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        mCallBackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun toastLoginError() = Toasty.error(this, getString(R.string.login_error)
            , Toast.LENGTH_SHORT, true).show()

    override fun toastLoginSuccess() = Toasty.success(this, getString(R.string.login_success)
            , Toast.LENGTH_SHORT, true).show()

    override fun showLeaderBoardDialog() = mLeaderBoardDialog.show()

    override fun hideLeaderBoardDialog() = mLeaderBoardDialog.cancel()

    override fun setLeaderBoardData(listData: List<Player>) = mLeaderBoardDialog.setData(listData)
}
