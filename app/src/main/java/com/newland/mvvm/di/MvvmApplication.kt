package com.newland.mvvm.di

import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import com.newland.core.di.module.AppModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.module.Module

class MvvmApplication : Application() {
    companion object {
        lateinit var instance: Context
    }

    override fun onCreate() {
        super.onCreate()
        instance = applicationContext
        val packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_META_DATA)
        val metadata = packageInfo.applicationInfo.metaData;
        val metadataKeys = metadata.keySet()
        val modules = arrayListOf(viewModelModule, apiModule, repositoryModule)
        for (metaKey in metadataKeys) {
            if (metadata.get(metaKey)?.equals("AppModule") == true) {
                val module = Class.forName(metaKey).newInstance() as AppModule
                modules.addAll(module.getConfigurationModules())
            }
        }
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(applicationContext)
            androidFileProperties()
            modules
        }
    }
}