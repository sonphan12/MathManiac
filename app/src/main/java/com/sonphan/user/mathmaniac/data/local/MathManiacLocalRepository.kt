package com.sonphan.user.mathmaniac.data.local

import android.content.Context
import com.sonphan.user.mathmaniac.data.SharedPreferencesConstants
import com.sonphan.user.mathmaniac.data.model.Player
import com.sonphan.user.mathmaniac.data.model.PlayerDao
import io.reactivex.Observable

class MathManiacLocalRepository constructor(val mPlayerDao: PlayerDao, val mContext: Context) : MathManiacLocalStore.Repository {
    override fun putPlayer(player: Player): Observable<Boolean> {
        return Observable.just(true)
                .map {
                    mPlayerDao.insertOrReplace(player)
                    true
                }
    }

    override fun putPlayers(players: List<Player>): Observable<Boolean> {
        return Observable.just(true)
                .map {
                    mPlayerDao.insertOrReplaceInTx(players)
                    true
                }
    }

    override fun getPlayer(fbId: Long): Observable<Player> {
        return Observable.create {
            val listPlayer = mPlayerDao.queryBuilder()
                    .where(PlayerDao.Properties.FbId.eq(fbId))
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

}