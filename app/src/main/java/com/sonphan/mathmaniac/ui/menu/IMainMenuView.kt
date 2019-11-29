package com.sonphan.mathmaniac.ui.menu

import com.sonphan.mathmaniac.data.model.Player

interface IMainMenuView {
    fun navigateToPlay()
    fun toastLoginError()
    fun toastLoginSuccess()
    fun showLeaderBoardDialog()
    fun hideLeaderBoardDialog()
    fun setLeaderBoardData(listData: List<Player>)
}