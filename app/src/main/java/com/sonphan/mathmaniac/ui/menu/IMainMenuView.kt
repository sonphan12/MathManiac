package com.sonphan.mathmaniac.ui.menu

import com.sonphan.mathmaniac.data.model.LocalPlayer

interface IMainMenuView {
    fun navigateToPlay()
    fun toastLoginError()
    fun toastLoginSuccess()
    fun showLeaderBoardDialog()
    fun hideLeaderBoardDialog()
    fun setLeaderBoardData(listData: List<LocalPlayer>)
}