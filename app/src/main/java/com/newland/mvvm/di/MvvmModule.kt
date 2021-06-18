package com.newland.mvvm.di

import com.newland.core.network.RetrofitService
import com.newland.mvvm.module.app.MainRepository
import com.newland.mvvm.module.app.MainViewModel
import com.newland.mvvm.network.api.MainApiService
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { MainViewModel(get()) }
}
val repositoryModule = module {
    single { MainRepository(get()) }
}
val apiModule = module {
    single { RetrofitService.createApiService(MainApiService::class.java) }
}