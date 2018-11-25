package com.sonphan.user.mathmaniac

import android.app.Application
import com.sonphan.user.mathmaniac.data.model.DaoMaster
import com.sonphan.user.mathmaniac.data.model.DaoSession
import com.sonphan.user.mathmaniac.data.model.PlayerDao

class AndroidApplication: Application() {
    private lateinit var mDaoMaster: DaoMaster
    private lateinit var mDaoSession: DaoSession
    private lateinit var mPlayerDao: PlayerDao
    override fun onCreate() {
        super.onCreate()
        initGreenDao()
    }

    private fun initGreenDao() {
        val helper = DaoMaster.DevOpenHelper(this, "players-db", null)
        val db = helper.writableDatabase
        mDaoMaster = DaoMaster(db)
        mDaoSession = mDaoMaster.newSession()
        mPlayerDao = mDaoSession.playerDao
    }

    fun getPlayerDao() = mPlayerDao
}