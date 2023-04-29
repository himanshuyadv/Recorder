package com.ansh.recorder.core.utils


import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

class LiveEvent<T> : MutableLiveData<T>() {
    private val alreadyReceivedCllRcc = AtomicBoolean(false)

    @androidx.annotation.MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {

        super.observe(owner) { tCllRcc ->
            if (alreadyReceivedCllRcc.compareAndSet(true, false)) {
                observer.onChanged(tCllRcc)
            }
        }
    }

    @androidx.annotation.MainThread
    override fun setValue(tCllRcc: T?) {

        alreadyReceivedCllRcc.set(true)
        super.setValue(tCllRcc)
    }

    @androidx.annotation.MainThread
    fun callCllRcc() {
        value = null
    }
}