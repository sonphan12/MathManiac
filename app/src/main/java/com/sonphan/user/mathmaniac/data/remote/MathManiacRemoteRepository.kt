package com.sonphan.user.mathmaniac.data.remote

import com.sonphan.user.mathmaniac.data.model.RemotePlayer
import io.reactivex.Observable

class MathManiacRemoteRepository : MathManiacRemoteStore.Repository {
    override fun putPlayer(player: RemotePlayer): Observable<Boolean> {
        return Observable.create {
            player.getPlayer().saveInBackground { e ->
                if (e == null) {
                    it.onNext(true)
                    it.onComplete()
                    return@saveInBackground
                }
                it.onError(e)
            }
        }
    }

}