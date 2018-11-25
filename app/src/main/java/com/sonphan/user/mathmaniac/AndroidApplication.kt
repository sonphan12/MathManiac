package com.sonphan.user.mathmaniac

import android.app.Application
import com.example.user.mathmaniac.R
import com.parse.Parse
import com.sonphan.user.mathmaniac.data.model.DaoMaster
import com.sonphan.user.mathmaniac.data.model.DaoSession
import com.sonphan.user.mathmaniac.data.model.LocalFacebookFriendDao
import com.sonphan.user.mathmaniac.data.model.LocalPlayerDao

class AndroidApplication: Application() {
    private lateinit var mDaoMaster: DaoMaster
    private lateinit var mDaoSession: DaoSession
    private lateinit var mLocalPlayerDao: LocalPlayerDao
    private lateinit var mLocalFacebookFriendDao: LocalFacebookFriendDao
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
        mLocalPlayerDao = mDaoSession.localPlayerDao
        mLocalFacebookFriendDao = mDaoSession.localFacebookFriendDao
    }

    fun getLocalPlayerDao() = mLocalPlayerDao
    fun getLocalFacebookFriendDao() = mLocalFacebookFriendDao
}