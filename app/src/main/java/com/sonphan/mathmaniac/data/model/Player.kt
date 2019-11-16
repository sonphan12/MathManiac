package com.sonphan.mathmaniac.data.model

import com.parse.ParseObject

data class Player(val id: Long, val name: String, val avatarUrl: String, val highScore: Int) {
    val player: ParseObject = ParseObject.create("Player")

    init {
        player.put("fbId", id)
        player.put("name", name)
        player.put("avatarUrl", avatarUrl)
        player.put("highScore", highScore)
    }

}