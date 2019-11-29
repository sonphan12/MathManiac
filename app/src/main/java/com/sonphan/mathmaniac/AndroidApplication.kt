package com.sonphan.mathmaniac

import android.app.Application
import com.facebook.Profile
import com.parse.Parse
import com.sonphan.mathmaniac.data.model.DaoMaster
import com.sonphan.mathmaniac.data.model.DaoSession
import com.sonphan.mathmaniac.data.model.FacebookFriendEntityDao
import com.sonphan.mathmaniac.data.model.PlayerEntityDao
import com.sonphan.mathmaniac.ultility.UserManager
import com.sonphan.user.mathmaniac.R

class AndroidApplication : Application() {
    private lateinit var mDaoMaster: DaoMaster
    private lateinit var mDaoSession: DaoSession
    lateinit var playerDao: PlayerEntityDao
        private set
    lateinit var mfacebookFriendDao: FacebookFriendEntityDao
        private set

    override fun onCreate() {
        super.onCreate()
        initGreenDao()
        initParse()
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
}