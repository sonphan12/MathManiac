package com.sonphan.user.mathmaniac.ui.menu

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.example.user.mathmaniac.R
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.Profile
import com.facebook.login.LoginResult
import com.sonphan.user.mathmaniac.AndroidApplication
import com.sonphan.user.mathmaniac.data.FacebookPermissionConstants
import com.sonphan.user.mathmaniac.data.local.MathManiacLocalRepository
import com.sonphan.user.mathmaniac.ui.play.PlayActivity
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_main_menu.*


class MainMenuActivity : AppCompatActivity(), IMainMenuView {
    private lateinit var mPresenter: MainMenuPresenter
    private val mCallBackManager = CallbackManager.Factory.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)
        mPresenter = MainMenuPresenter(MathManiacLocalRepository((this.application as AndroidApplication).getPlayerDao(), this.applicationContext))
        btnPlay.setOnClickListener { mPresenter.onPlayClicked() }

        initLoginFacebook()
    }

    private fun initLoginFacebook() {
        fbLoginButton.setReadPermissions(FacebookPermissionConstants.USER_FRIENDS)
        fbLoginButton.registerCallback(mCallBackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                val profile = Profile.getCurrentProfile()
                mPresenter.onLoginSuccess(profile.id, profile.name, profile.getProfilePictureUri(60, 60))
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
}
