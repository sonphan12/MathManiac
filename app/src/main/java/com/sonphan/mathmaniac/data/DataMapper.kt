package com.sonphan.mathmaniac.data

import com.facebook.GraphResponse
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.sonphan.mathmaniac.data.model.FacebookFriend
import com.sonphan.mathmaniac.data.model.FacebookFriendEntity
import com.sonphan.mathmaniac.data.model.Player
import com.sonphan.mathmaniac.data.model.PlayerEntity
import java.util.*
import kotlin.collections.ArrayList

fun FacebookFriend.toEntity() = FacebookFriendEntity(
        this.fbId,
        this.displayName,
        this.avatarUrl
)

fun Player.toEntity() = PlayerEntity(
        this.id,
        this.name,
        this.avatarUrl,
        this.highScore
)

fun PlayerEntity.toPlayer() = Player(
        this.fbId,
        this.displayName,
        this.avatarUrl,
        this.highScore
)

fun FacebookFriendEntity.toFacebookFriend() = FacebookFriend(
        this.fbId,
        this.displayName,
        this.avatarUrl
)

fun GraphResponse.toListFbFriend(): List<FacebookFriend> {
    val parser = JsonParser()
    val jsonObject = parser.parse(this.rawResponse).asJsonObject
    if (!jsonObject.has("data")) {
        return Collections.emptyList()
    }
    val listFbFriend = ArrayList<FacebookFriend>()
    val data = jsonObject.get("data").asJsonArray
    for (userJson in data) {
        val userJsonObject = userJson.asJsonObject
        val user = Gson().fromJson(userJsonObject, FacebookFriend::class.java)
        listFbFriend.add(user)
    }
    return listFbFriend
}