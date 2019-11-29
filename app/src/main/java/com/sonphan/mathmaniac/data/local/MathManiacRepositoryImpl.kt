package com.sonphan.mathmaniac.data.local

import android.util.Log
import com.facebook.GraphRequest
import com.parse.ParseObject
import com.parse.ParseQuery
import com.sonphan.mathmaniac.AndroidApplication
import com.sonphan.mathmaniac.data.model.FacebookFriend
import com.sonphan.mathmaniac.data.model.FacebookFriendEntityDao
import com.sonphan.mathmaniac.data.model.Player
import com.sonphan.mathmaniac.data.model.PlayerEntityDao
import com.sonphan.mathmaniac.data.toEntity
import com.sonphan.mathmaniac.data.toFacebookFriend
import com.sonphan.mathmaniac.data.toListFbFriend
import com.sonphan.mathmaniac.data.toPlayer
import com.sonphan.mathmaniac.ultility.UserManager
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class MathManiacRepositoryImpl private constructor(private val playerDao: PlayerEntityDao,
                                                   private val facebookFriendDao: FacebookFriendEntityDao
) : MathManiacRepository {

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
                    .flatMap { updateParseObject(it) }
                    .flatMap { parseObject ->
                        Observable.create<Boolean> { emitter ->
                            parseObject.saveInBackground { e ->
                                if (e == null) {
                                    emitter.onNext(true)
                                } else {
                                    emitter.onError(e)
                                }
                            }
                        }
                    }

    private fun updateParseObject(player: Player): Observable<ParseObject> =
            Observable.create {
                ParseQuery.getQuery<ParseObject>("Player")
                        .whereEqualTo("fbId", player.id)
                        .getFirstInBackground { result, e ->
                            if (e == null) {
                                it.onNext(result.apply {
                                    put("highScore", player.highScore)
                                })
                            } else {
                                it.onNext(player.player)
                            }
                            it.onComplete()
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

    override fun getHighScore(fbId: Long): Observable<Int> =
            getPlayer(fbId)
                    .map { it.highScore }

    override fun setHighScore(fbId: Long, highScore: Int): Observable<Boolean> =
            getPlayer(fbId)
                    .flatMap {
                        putPlayers(
                                Player(
                                        it.id,
                                        it.name,
                                        it.avatarUrl,
                                        highScore
                                )
                        )
                    }
                    .map { true }

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

    companion object {
        val instance: MathManiacRepository by lazy {
            MathManiacRepositoryImpl(
                    AndroidApplication.instance.playerDao,
                    AndroidApplication.instance.mfacebookFriendDao)
        }
    }

}