package com.sonphan.user.mathmaniac.ui

open class BasePresenter<V> {
    protected var mView: V? = null
    fun attachView(view: V) {
        mView = view
    }
    fun detachView() {
        mView = null
    }
}