package com.sonphan.user.mathmaniac.data.remote

import com.parse.ParseObject
import com.parse.ParseQuery
import com.sonphan.mathmaniac.data.model.RemotePlayer
import io.reactivex.Observable

class MathManiacRemoteRepository : MathManiacRemoteStore.Repository {
    override fun putPlayer(player: RemotePlayer): Observable<Boolean> {
        return Observable.create {
            val query: ParseQuery<ParseObject> = ParseQuery.getQuery("Player")
            query.whereEqualTo("fbId", player.id)
            query.getFirstInBackground { `object`, _ ->
                if (`object` != null) {
                    `object`.put("fbId", player.id)
                    `object`.put("name", player.name)
                    `object`.put("avatarUrl", player.avatarUrl)
                    `object`.put("highScore", player.highScore)
                    `object`.saveInBackground { e ->
                        if (e == null) {
                            it.onNext(true)
                            it.onComplete()
                            return@saveInBackground
                        }
                        it.onError(e)
                    }
                } else {
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
    }

}