package com.sonphan.mathmaniac.ui.menu

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.bumptech.glide.Glide
import com.sonphan.mathmaniac.data.model.Player
import kotlinx.android.synthetic.main.item_player.view.*

class PlayerHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(player: Player, position: Int) = with(itemView) {
        itemView.txtRank.text = (position + 1).toString()
        itemView.txtName.text = player.name
        itemView.txtScore.text = player.highScore.toString()
        Glide.with(itemView).load(player.avatarUrl).into(itemView.imgAvatar)
    }

}