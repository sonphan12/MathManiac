package com.sonphan.user.mathmaniac.ui.menu

import android.net.Uri
import android.util.Log
import com.sonphan.user.mathmaniac.data.local.MathManiacLocalRepository
import com.sonphan.user.mathmaniac.data.model.Player
import com.sonphan.user.mathmaniac.ui.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainMenuPresenter constructor(private val mLocalRepository: MathManiacLocalRepository) : BasePresenter<IMainMenuView>() {
    fun onPlayClicked() {
        mView?.navigateToPlay()
    }

    fun onLoginSuccess(id: String, name: String, profilePictureUri: Uri) {
        mLocalRepository.getCurrentHighScore()
                .doOnSubscribe { mView?.toastLoginSuccess() }
                .flatMap {
                    val currentPlayer = Player(id.toLong(), name, profilePictureUri.toString(), it)
                    Log.d(javaClass.simpleName, "Put current player $currentPlayer")
                    mLocalRepository.putPlayer(currentPlayer)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe()
    }

    fun onLoginFailed() = mView?.toastLoginError()
}
