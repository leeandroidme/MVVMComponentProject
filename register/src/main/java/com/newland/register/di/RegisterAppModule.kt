package com.newland.register.di

import com.newland.core.di.module.AppModule
import org.koin.core.module.Module

/**
 * @author: leellun
 * @data: 18/6/2021.
 *
 */
class RegisterAppModule : AppModule {
    override fun getConfigurationModules(): List<Module> =
        arrayListOf(viewModelModule, apiModule, repositoryModule, databaseModule)
}