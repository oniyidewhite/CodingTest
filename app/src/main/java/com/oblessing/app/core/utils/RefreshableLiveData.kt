package com.oblessing.app.core.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData


class RefreshableLiveData<T>(private val refreshCallback: (forceReload: Boolean, receiver: MutableLiveData<T>) -> Unit) {
    private val _liveData = MutableLiveData<T>()

    fun refresh(forceReload: Boolean = false) {
        refreshCallback(forceReload, _liveData)
    }

    val toLiveData: LiveData<T>
        get() = _liveData
}


