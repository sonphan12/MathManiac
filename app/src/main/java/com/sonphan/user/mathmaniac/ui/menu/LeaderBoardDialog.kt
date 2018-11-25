package com.sonphan.user.mathmaniac.ui.menu

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.user.mathmaniac.R
import com.sonphan.user.mathmaniac.data.model.LocalPlayer
import kotlinx.android.synthetic.main.dialog_leaderboard.*

class LeaderBoardDialog constructor(private val mContext: Context) : Dialog(mContext) {
    lateinit var adapter: LeaderBoardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_leaderboard)
        adapter = LeaderBoardAdapter(mContext, ArrayList())
        rvLeaderBoard.adapter = adapter
        rvLeaderBoard.layoutManager = LinearLayoutManager(mContext)
    }

    fun setData(listData: List<LocalPlayer>) = adapter.setListData(listData)

    class LeaderBoardAdapter constructor(val mContext: Context, var mListData: List<LocalPlayer>)
        : RecyclerView.Adapter<PlayerHolder>() {
        override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): PlayerHolder {
            val view = LayoutInflater.from(mContext).inflate(R.layout.item_player, viewGroup, false)
            return PlayerHolder(view)
        }

        override fun getItemCount(): Int {
            return mListData.size
        }

        override fun onBindViewHolder(holder: PlayerHolder, position: Int) {
            val player = mListData[position]
            holder.bind(player, position)
        }


        override fun getItemId(position: Int): Long {
            val player = mListData[position]
            return player.fbId
        }

        fun setListData(listData: List<LocalPlayer>) {
            this.mListData = listData
            notifyDataSetChanged()
        }
    }
}