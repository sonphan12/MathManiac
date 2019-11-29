package com.sonphan.mathmaniac.ui.menu

import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import com.facebook.AccessToken
import com.facebook.Profile
import com.sonphan.mathmaniac.data.SharedPreferencesConstants
import com.sonphan.mathmaniac.data.local.MathManiacRepository
import com.sonphan.mathmaniac.data.model.Player
import com.sonphan.mathmaniac.ui.BasePresenter
import com.sonphan.mathmaniac.ultility.UserManager
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainMenuPresenter constructor(private val repository: MathManiacRepository, private val sharefPref: SharedPreferences) : BasePresenter<IMainMenuView>() {
    fun onPlayClicked() {
        view?.navigateToPlay()
    }

    fun onLoginSuccess(profile: Profile, currentAccessToken: AccessToken) {
        view?.toastLoginSuccess()
        putCurrentUser(profile.id, profile.name, profile.getProfilePictureUri(60, 60), currentAccessToken)
        fetchListFacebookFriends()
    }

    private fun putCurrentUser(id: String, name: String, profilePictureUri: Uri, accessToken: AccessToken) {
        UserManager.initUser(id.toLong(), name, profilePictureUri.toString(), accessToken)

        getHighScoreFromSharedPref()
                .flatMap {
                    val currentPlayer = Player(id.toLong(), name, profilePictureUri.toString(), it)
                    Log.d(javaClass.simpleName, "Put current player $currentPlayer")
                    repository.putPlayers(currentPlayer)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe()

    }

    private fun getHighScoreFromSharedPref(): Observable<Int> =
            Observable.create {
                it.onNext(sharefPref.getInt(SharedPreferencesConstants.HIGH_SCORE_KEY, 0))
                it.onComplete()
            }

    private fun fetchListFacebookFriends() {
        repository.fetchAllFacebookFriends()
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }

    fun onLoginFailed() = view?.toastLoginError()

    fun onRankClicked() {
        UserManager.user?.let {
            compositeDisposable.add(
                    repository.getPlayer(it.fbId)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe { localPlayer ->
                                val listData = ArrayList<Player>()
                                listData.add(localPlayer)
                                view?.setLeaderBoardData(listData)
                                view?.showLeaderBoardDialog()
                            }
            )
        }
    }
}
