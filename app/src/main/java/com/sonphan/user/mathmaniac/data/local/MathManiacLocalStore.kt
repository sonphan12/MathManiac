package com.sonphan.user.mathmaniac.data.local

import com.sonphan.user.mathmaniac.data.model.Player
import io.reactivex.Observable

interface MathManiacLocalStore {
    interface Repository {
        fun putPlayer(player: Player): Observable<Boolean>
        fun putPlayers(players: List<Player>): Observable<Boolean>
        fun getPlayer(fbId: Long): Observable<Player>
        fun getCurrentHighScore(): Observable<Int>
    }
}