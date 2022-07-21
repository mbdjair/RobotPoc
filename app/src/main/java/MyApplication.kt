package com.djair.robotpoc

import android.app.Application
import com.djair.robotpoc.com.djair.robotpoc.di.applicationModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@MyApplication)
            modules(applicationModules)
        }
    }

    override fun onTerminate() {
        stopKoin()
        super.onTerminate()
    }
}
