package com.sonphan.user.mathmaniac.ui.menu

import android.net.Uri
import android.util.Log
import com.facebook.AccessToken
import com.sonphan.user.mathmaniac.data.local.MathManiacLocalStore
import com.sonphan.user.mathmaniac.data.model.LocalPlayer
import com.sonphan.user.mathmaniac.data.model.RemotePlayer
import com.sonphan.user.mathmaniac.data.remote.MathManiacFacebookStore
import com.sonphan.user.mathmaniac.data.remote.MathManiacRemoteStore
import com.sonphan.user.mathmaniac.ui.BasePresenter
import com.sonphan.user.mathmaniac.ultility.UserUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainMenuPresenter constructor(private val mLocalRepository: MathManiacLocalStore.Repository,
                                    private val mRemoteFacebookRepository: MathManiacFacebookStore.Repository,
                                    private val mRemoteRepository: MathManiacRemoteStore.Repository) : BasePresenter<IMainMenuView>() {
    private val mSubscription = CompositeDisposable()
    fun onPlayClicked() {
        mView?.navigateToPlay()
    }

    fun onLoginSuccess(id: String, name: String, profilePictureUri: Uri, currentAccessToken: AccessToken) {
        putCurrentUserLocal(id, name, profilePictureUri, currentAccessToken)
        putCurrentUserRemote(id, name, profilePictureUri)
        putListFacebookFriends()
    }

    private fun putCurrentUserRemote(id: String, name: String, profilePictureUri: Uri) {
        mLocalRepository.getCurrentHighScore()
                .flatMap {
                    val currentPlayer = RemotePlayer(id.toLong(), name, profilePictureUri.toString(), it)
                    mRemoteRepository.putPlayer(currentPlayer)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe()
    }

    private fun putListFacebookFriends() {
        mRemoteFacebookRepository.getAllFacebookFriends()
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }

    fun onLoginFailed() = mView?.toastLoginError()

    private fun putCurrentUserLocal(id: String, name: String, profilePictureUri: Uri, currentAccessToken: AccessToken) {
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

    fun onRankClicked() {
        if (UserUtil.getUser() == null) {
            return
        }
        mSubscription.add(
                mLocalRepository.getPlayer(UserUtil.getUser()!!.fbId)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe { localPlayer ->
                            run {
                                val listData = ArrayList<LocalPlayer>()
                                listData.add(localPlayer)
                                mView?.setLeaderBoardData(listData)
                                mView?.showLeaderBoardDialog()
                            }
                        }
        ) }
}
