package com.sonphan.user.mathmaniac.data.local

import com.sonphan.user.mathmaniac.data.model.LocalFacebookFriend
import com.sonphan.user.mathmaniac.data.model.LocalPlayer
import io.reactivex.Observable

interface MathManiacLocalStore {
    interface Repository {
        fun putPlayer(player: LocalPlayer): Observable<Boolean>
        fun putPlayers(players: List<LocalPlayer>): Observable<Boolean>
        fun getPlayer(fbId: Long): Observable<LocalPlayer>
        fun getCurrentHighScore(): Observable<Int>
        fun putFacebookFriends(friends: List<LocalFacebookFriend>): Observable<Boolean>
        fun getAllFacebookFriends(): Observable<List<LocalFacebookFriend>>
    }
}