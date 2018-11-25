package com.sonphan.user.mathmaniac.data.model

import com.parse.ParseObject

class RemotePlayer(id: Long, name: String, avatarUrl: String, highScore: Int) {
    private val mPlayer = ParseObject.create("Player")

    init {
        mPlayer.put("fbId", id)
        mPlayer.put("name", name)
        mPlayer.put("avatarUrl", avatarUrl)
        mPlayer.put("highScore", highScore)
    }

    fun getPlayer() = mPlayer

}