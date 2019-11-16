package com.sonphan.user.mathmaniac.data.remote

import android.util.Log
import com.facebook.GraphRequest
import com.facebook.GraphResponse
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.sonphan.mathmaniac.data.model.RemoteFacebookFriend
import com.sonphan.mathmaniac.ultility.UserUtil
import io.reactivex.Observable
import java.util.*
import kotlin.collections.ArrayList

class MathManiacFacebookRepository : MathManiacFacebookStore.Repository {
    override fun getAllFacebookFriends(): Observable<List<RemoteFacebookFriend>> {
        return Observable.create {
            GraphRequest.newGraphPathRequest(
                    UserUtil.getUser()?.accessToken,
                    "/" + UserUtil.getUser()?.fbId + "/friends"
            )
            { response ->
                run {
                    val listFbFriend = responseToListUser(response)
                    Log.d(javaClass.simpleName, "List fb friends: " + listFbFriend.toString())
                    it.onNext(listFbFriend)
                    it.onComplete()
                }
            }.executeAsync()
        }
    }

    private fun responseToListUser(response: GraphResponse): List<RemoteFacebookFriend> {
        val parser = JsonParser()
        val jsonObject = parser.parse(response.rawResponse).asJsonObject
        if (!jsonObject.has("data")) {
            return Collections.emptyList<RemoteFacebookFriend>()
        }
        val listFbFriend = ArrayList<RemoteFacebookFriend>()
        val data = jsonObject.get("data").asJsonArray
        for (userJson in data) {
            val userJsonObject = userJson.asJsonObject
            val user = Gson().fromJson(userJsonObject, RemoteFacebookFriend::class.java)
            listFbFriend.add(user)
        }
        return listFbFriend
    }
}