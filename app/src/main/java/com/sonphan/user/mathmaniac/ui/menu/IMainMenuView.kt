package com.sonphan.user.mathmaniac.ui.menu

import com.sonphan.user.mathmaniac.data.model.LocalPlayer

interface IMainMenuView {
    fun navigateToPlay()
    fun toastLoginError()
    fun toastLoginSuccess()
    fun showLeaderBoardDialog()
    fun hideLeaderBoardDialog()
    fun setLeaderBoardData(listData: List<LocalPlayer>)
}