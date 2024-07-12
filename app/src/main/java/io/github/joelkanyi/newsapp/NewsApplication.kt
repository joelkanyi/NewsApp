package io.github.joelkanyi.newsapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import io.github.joelkanyi.data.BuildConfig
import timber.log.Timber

@HiltAndroidApp
class NewsApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initTimber()
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
