package com.sonphan.user.mathmaniac.data.model

import com.google.gson.annotations.SerializedName

data class RemoteFacebookFriend constructor(@SerializedName("id") val fbId: Long,
                                            @SerializedName("name") val displayName: String,
                                            @SerializedName("picture") val avatarUrl: String)