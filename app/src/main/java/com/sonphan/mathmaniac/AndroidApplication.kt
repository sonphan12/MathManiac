package com.sonphan.mathmaniac

import android.app.Application
import android.content.Context
import android.util.Log
import com.facebook.AccessToken
import com.facebook.Profile
import com.parse.Parse
import com.sonphan.mathmaniac.data.SharedPreferencesConstants
import com.sonphan.mathmaniac.data.local.MathManiacRepositoryImpl
import com.sonphan.mathmaniac.data.model.*
import com.sonphan.mathmaniac.ultility.UserManager
import com.sonphan.user.mathmaniac.R
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class AndroidApplication : Application() {
    private lateinit var mDaoMaster: DaoMaster
    private lateinit var mDaoSession: DaoSession
    lateinit var playerDao: PlayerEntityDao
        private set
    lateinit var mfacebookFriendDao: FacebookFriendEntityDao
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this
        initGreenDao()
        initParse()

        Profile.getCurrentProfile()?.let {
            initLoggedInUser(it)
        }
    }

    private fun initLoggedInUser(profile: Profile) {
        UserManager.initUser(profile.id.toLong(), profile.name,
                profile.getProfilePictureUri(60, 60).toString(), AccessToken.getCurrentAccessToken())

        getHighScoreFromSharedPref()
                .flatMap {
                    MathManiacRepositoryImpl.instance.setHighScore(UserManager.user!!.fbId, it)
                }
                .subscribeOn(Schedulers.io())
                .subscribe()
    }

    private fun initParse() {
        Parse.initialize(Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                // if defined
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build()
        )
    }

    private fun initGreenDao() {
        val helper = DaoMaster.DevOpenHelper(this, "mathmaniac-db", null)
        val db = helper.writableDatabase
        mDaoMaster = DaoMaster(db)
        mDaoSession = mDaoMaster.newSession()
        playerDao = mDaoSession.playerEntityDao
        mfacebookFriendDao = mDaoSession.facebookFriendEntityDao
    }

    private fun getHighScoreFromSharedPref(): Observable<Int> =
            Observable.create {
                it.onNext(getSharedPreferences(SharedPreferencesConstants.HIGH_SCORE_NAME, Context.MODE_PRIVATE)
                        .getInt(SharedPreferencesConstants.HIGH_SCORE_KEY, 0))
                it.onComplete()
            }

    companion object {
        lateinit var instance: AndroidApplication
    }
}