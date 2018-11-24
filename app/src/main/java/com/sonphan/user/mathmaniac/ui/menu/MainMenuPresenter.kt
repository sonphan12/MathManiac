package com.sonphan.user.mathmaniac.ui.menu

import com.sonphan.user.mathmaniac.ui.BasePresenter

class MainMenuPresenter: BasePresenter<IMainMenuView>() {
    fun onPlayClicked() {
        mView?.navigateToPlay()
    }

    fun onLoginSuccess() {
        mView?.toastLoginSuccess()
    }
    fun onLoginFailed() = mView?.toastLoginError()
}
