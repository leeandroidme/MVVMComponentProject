package com.newland.core.di.module

import org.koin.core.module.Module

/**
 * @author: leellun
 * @data: 18/6/2021.
 *
 */
interface AppModule {
    fun getConfigurationModules(): List<Module>
}