package com.example.user.mathmaniac.ui.menu

import com.example.user.mathmaniac.ui.BasePresenter

class MainMenuPresenter: BasePresenter<IMainMenuView>() {
    fun onPlayClicked() {
        mView?.navigateToPlay()
    }
}
