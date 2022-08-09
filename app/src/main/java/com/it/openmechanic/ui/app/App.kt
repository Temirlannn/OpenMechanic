package com.it.openmechanic.ui.app

import android.app.Application
import com.google.firebase.FirebaseApp
import com.it.openmechanic.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(applicationContext)
        startKoin {
            androidContext(this@App)
            modules(listOf(appModule))
        }
    }
}