package com.sonphan.user.mathmaniac.data.model

import com.facebook.AccessToken

class User constructor(val fbId: Long,
                       val displayName: String,
                       val avatarUrl: String,
                       val accessToken: AccessToken)