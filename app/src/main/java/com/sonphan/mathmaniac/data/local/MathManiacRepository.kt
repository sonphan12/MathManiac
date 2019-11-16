package com.sonphan.mathmaniac.data.local

import com.sonphan.mathmaniac.data.model.FacebookFriend
import com.sonphan.mathmaniac.data.model.Player
import io.reactivex.Observable

interface MathManiacRepository {
    fun putPlayers(vararg players: Player): Observable<Boolean>
    fun getPlayer(fbId: Long): Observable<Player>
    fun getCurrentHighScore(): Observable<Int>
    fun putFacebookFriends(friends: List<FacebookFriend>): Observable<Boolean>
    fun getAllFacebookFriends(): Observable<List<FacebookFriend>>
    fun fetchAllFacebookFriends(): Observable<Boolean>
}