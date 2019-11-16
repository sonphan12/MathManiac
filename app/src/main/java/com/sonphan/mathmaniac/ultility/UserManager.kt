package com.sonphan.mathmaniac.ultility

import com.facebook.AccessToken
import com.sonphan.mathmaniac.data.model.User

object UserManager {
    var user: User? = null
        private set

    fun initUser(fbId: Long, displayName: String,
                 avatarUrl: String, accessToken: AccessToken) {
        user = User(fbId, displayName, avatarUrl, accessToken)
    }
}