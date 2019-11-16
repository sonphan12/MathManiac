package com.sonphan.mathmaniac.ui

import io.reactivex.disposables.CompositeDisposable

abstract class BasePresenter<V> {
    protected var view: V? = null

    protected var compositeDisposable = CompositeDisposable()

    fun attachView(view: V) {
        this.view = view
    }
    fun detachView() {
        view = null
        compositeDisposable.dispose()
    }
}