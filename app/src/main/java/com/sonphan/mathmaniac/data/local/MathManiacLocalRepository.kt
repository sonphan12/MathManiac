package com.sonphan.mathmaniac.data.local

import android.content.Context
import com.sonphan.mathmaniac.data.SharedPreferencesConstants
import com.sonphan.mathmaniac.data.model.LocalFacebookFriend
import com.sonphan.mathmaniac.data.model.LocalFacebookFriendDao
import com.sonphan.mathmaniac.data.model.LocalPlayer
import com.sonphan.mathmaniac.data.model.LocalPlayerDao
import io.reactivex.Observable

class MathManiacLocalRepository constructor(val mLocalPlayerDao: LocalPlayerDao,
                                            val mLocalFacebookFriendDao: LocalFacebookFriendDao,
                                            val mContext: Context) : MathManiacLocalStore.Repository {
    override fun putPlayer(player: LocalPlayer): Observable<Boolean> {
        return Observable.just(true)
                .map {
                    mLocalPlayerDao.insertOrReplace(player)
                    true
                }
    }

    override fun putPlayers(players: List<LocalPlayer>): Observable<Boolean> {
        return Observable.just(true)
                .map {
                    mLocalPlayerDao.insertOrReplaceInTx(players)
                    true
                }
    }

    override fun getPlayer(fbId: Long): Observable<LocalPlayer> {
        return Observable.create {
            val listPlayer = mLocalPlayerDao.queryBuilder()
                    .where(LocalPlayerDao.Properties.FbId.eq(fbId))
                    .list()
            if (!listPlayer.isEmpty()) {
                it.onNext(listPlayer[0])
            }
            it.onComplete()
        }
    }

    override fun getCurrentHighScore(): Observable<Int> {
        return Observable.create {
            val sharedPref = mContext.getSharedPreferences(SharedPreferencesConstants.HIGH_SCORE_NAME, Context.MODE_PRIVATE)
            it.onNext(sharedPref.getInt(SharedPreferencesConstants.HIGH_SCORE_KEY, 0))
            it.onComplete()
        }
    }

    override fun putFacebookFriends(friends: List<LocalFacebookFriend>): Observable<Boolean> {
        return Observable.just(true)
                .map { mLocalFacebookFriendDao.insertOrReplaceInTx(friends)
                true}
    }

    override fun getAllFacebookFriends(): Observable<List<LocalFacebookFriend>> {
        return Observable.create{
            val friends = mLocalFacebookFriendDao.queryBuilder()
                    .list()
            it.onNext(friends)
            it.onComplete()
        }
    }

}