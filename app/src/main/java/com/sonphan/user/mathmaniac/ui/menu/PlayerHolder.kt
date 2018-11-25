package com.sonphan.user.mathmaniac.ui.menu

import android.support.v7.widget.RecyclerView
import android.view.View
import com.bumptech.glide.Glide
import com.sonphan.user.mathmaniac.data.model.LocalPlayer
import kotlinx.android.synthetic.main.item_player.view.*

class PlayerHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(player: LocalPlayer, position: Int) = with(itemView) {
        itemView.txtRank.text = position.toString()
        itemView.txtName.text = player.displayName
        itemView.txtScore.text = player.highScore.toString()
        Glide.with(itemView).load(player.getAvatarUrl()).into(itemView.imgAvatar)
    }

}