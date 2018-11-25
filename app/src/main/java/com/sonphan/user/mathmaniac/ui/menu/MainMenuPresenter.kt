package com.sonphan.user.mathmaniac.ui.menu

import android.net.Uri
import android.util.Log
import com.facebook.AccessToken
import com.sonphan.user.mathmaniac.data.local.MathManiacLocalRepository
import com.sonphan.user.mathmaniac.data.model.LocalPlayer
import com.sonphan.user.mathmaniac.data.remote.MathManiacFacebookRepository
import com.sonphan.user.mathmaniac.ui.BasePresenter
import com.sonphan.user.mathmaniac.ultility.UserUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainMenuPresenter constructor(private val mLocalRepository: MathManiacLocalRepository,
                                    private val mRemoteFacebookRepository: MathManiacFacebookRepository) : BasePresenter<IMainMenuView>() {
    fun onPlayClicked() {
        mView?.navigateToPlay()
    }

    fun onLoginSuccess(id: String, name: String, profilePictureUri: Uri, currentAccessToken: AccessToken) {
        putCurrentUser(id, name, profilePictureUri, currentAccessToken)
        putListFacebookFriends()
    }

    private fun putListFacebookFriends() {
        mRemoteFacebookRepository.getAllFacebookFriends()
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }

    fun onLoginFailed() = mView?.toastLoginError()

    private fun putCurrentUser(id: String, name: String, profilePictureUri: Uri, currentAccessToken: AccessToken) {
        UserUtil.initUser(id.toLong(), name, profilePictureUri.toString(), currentAccessToken)
        mLocalRepository.getCurrentHighScore()
                .doOnSubscribe { mView?.toastLoginSuccess() }
                .flatMap {
                    val currentPlayer = LocalPlayer(id.toLong(), name, profilePictureUri.toString(), it)
                    Log.d(javaClass.simpleName, "Put current player $currentPlayer")
                    mLocalRepository.putPlayer(currentPlayer)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe()
    }
}
