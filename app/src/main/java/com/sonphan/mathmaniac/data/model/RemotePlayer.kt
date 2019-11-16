package com.sonphan.mathmaniac.data.model

import com.parse.ParseObject

data class RemotePlayer(val id: Long, val name: String, val avatarUrl: String, val highScore: Int) {
    private val mPlayer = ParseObject.create("Player")

    init {
        mPlayer.put("fbId", id)
        mPlayer.put("name", name)
        mPlayer.put("avatarUrl", avatarUrl)
        mPlayer.put("highScore", highScore)
    }

    fun getPlayer() = mPlayer

}