package com.sonphan.user.mathmaniac.data.remote

import com.sonphan.user.mathmaniac.data.model.RemoteFacebookFriend
import io.reactivex.Observable

interface MathManiacFacebookStore {
    interface Repository {
        fun getAllFacebookFriends(): Observable<List<RemoteFacebookFriend>>
    }
}