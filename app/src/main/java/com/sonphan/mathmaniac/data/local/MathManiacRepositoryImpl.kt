package com.sonphan.mathmaniac.data.local

import android.content.Context
import android.util.Log
import com.facebook.GraphRequest
import com.parse.ParseException
import com.parse.ParseObject
import com.parse.ParseQuery
import com.sonphan.mathmaniac.data.*
import com.sonphan.mathmaniac.data.model.*
import com.sonphan.mathmaniac.ultility.UserManager
import io.reactivex.Observable
import io.reactivex.ObservableSource
import java.util.concurrent.TimeUnit

class MathManiacRepositoryImpl constructor(private val playerDao: PlayerEntityDao,
                                           private val facebookFriendDao: FacebookFriendEntityDao,
                                           private val context: Context) : MathManiacRepository {

    override fun putPlayers(vararg players: Player): Observable<Boolean> =
            Observable.create<Boolean> {
                playerDao.insertOrReplaceInTx(players.map { player -> player.toEntity() })
                it.onNext(true)
                it.onComplete()
            }
                    .mergeWith(putPlayersToParse(*players))
                    .reduce { _, _ -> true }
                    .toObservable()

    private fun putPlayersToParse(vararg players: Player): Observable<Boolean> =
            Observable.fromIterable(players.toList())
                    .flatMap { player ->
                        Observable.create<Boolean> {
                            player.player.saveInBackground { e ->
                                if (e != null) {
                                    it.onError(e)
                                } else {
                                    it.onNext(true)
                                }
                            }
                        }
                    }


    override fun getPlayer(fbId: Long): Observable<Player> {
        return Observable.create {
            val listPlayer = playerDao.queryBuilder()
                    .where(PlayerEntityDao.Properties.FbId.eq(fbId))
                    .list()
            if (listPlayer.isNotEmpty()) {
                it.onNext(listPlayer[0].toPlayer())
            }
            it.onComplete()
        }
    }

    override fun getCurrentHighScore(): Observable<Int> {
        return Observable.create {
            val sharedPref = context.getSharedPreferences(SharedPreferencesConstants.HIGH_SCORE_NAME, Context.MODE_PRIVATE)
            it.onNext(sharedPref.getInt(SharedPreferencesConstants.HIGH_SCORE_KEY, 0))
            it.onComplete()
        }
    }

    override fun putFacebookFriends(friends: List<FacebookFriend>): Observable<Boolean> {
        return Observable.create {
            facebookFriendDao.insertOrReplaceInTx(friends.map { facebookFriend -> facebookFriend.toEntity() })
            it.onNext(true)
            it.onComplete()
        }
    }

    override fun getAllFacebookFriends(): Observable<List<FacebookFriend>> {
        return Observable.create {
            val friends = facebookFriendDao.queryBuilder()
                    .list()
            it.onNext(friends.map { friend -> friend.toFacebookFriend() })
            it.onComplete()
        }
    }

    override fun fetchAllFacebookFriends(): Observable<Boolean> =
            Observable.create<List<FacebookFriend>> {
                GraphRequest.newGraphPathRequest(
                        UserManager.user?.accessToken,
                        "/" + UserManager.user?.fbId + "/friends"
                ) { response ->
                    run {
                        val listFbFriend = response.toListFbFriend()
                        Log.d(javaClass.simpleName, "List fb friends: $listFbFriend")
                        it.onNext(listFbFriend)
                        it.onComplete()
                    }
                }.executeAsync()
            }
                    .timeout(5000, TimeUnit.MILLISECONDS)
                    .flatMap { listFbFriends ->
                        putFacebookFriends(listFbFriends)
                    }

}