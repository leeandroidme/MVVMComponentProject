package com.newland.register.di

import android.app.Application
import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class RegisterApplication : Application() {
    companion object {
        lateinit var instance: Context
    }

    override fun onCreate() {
        super.onCreate()
        instance = applicationContext
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(applicationContext)
            androidFileProperties()
            modules(viewModelModule, apiModule, repositoryModule, databaseModule)
        }
    }
}