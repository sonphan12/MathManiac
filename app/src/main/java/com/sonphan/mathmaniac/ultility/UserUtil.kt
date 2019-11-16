package com.sonphan.mathmaniac.ultility

import com.facebook.AccessToken
import com.sonphan.mathmaniac.data.model.User

class UserUtil {
    companion object {
        private var mUser: User? = null
        fun getUser() = mUser
        fun initUser(fbId: Long, displayName: String,
                     avatarUrl: String, accessToken: AccessToken) {
            mUser = User(fbId, displayName, avatarUrl, accessToken)
        }
    }
}