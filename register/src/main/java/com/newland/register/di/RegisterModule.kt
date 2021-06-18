package com.newland.register.di

import com.newland.core.network.RetrofitService
import com.newland.register.db.HistoryDatabase
import com.newland.register.db.dao.HistoryDao
import com.newland.register.module.register.RegisterRepository
import com.newland.register.module.register.RegisterViewModel
import com.newland.register.network.api.RegisterApiService
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { RegisterViewModel(get()) }
}
val repositoryModule = module {
    single { RegisterRepository(get()) }
}
val apiModule = module {
    single { RetrofitService.createApiService(RegisterApiService::class.java) }
}
val databaseModule = module {
    single { HistoryDatabase.getInstance(get()) }
    single { createHistoryDao(get()) }
}

internal fun createHistoryDao(database: HistoryDatabase): HistoryDao {
    return database.historyDao()
}