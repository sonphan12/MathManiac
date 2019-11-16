package com.sonphan.user.mathmaniac.data.remote

import com.sonphan.mathmaniac.data.model.RemotePlayer
import io.reactivex.Observable

interface MathManiacRemoteStore {

    interface Repository {
        fun putPlayer(player: RemotePlayer): Observable<Boolean>
    }
}