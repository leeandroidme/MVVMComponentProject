package com.newland.register.module.register

import com.newland.register.db.dao.HistoryDao
import com.newland.register.db.entity.History
import com.newland.register.network.api.RegisterApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent

class RegisterRepository(private val api: RegisterApiService) {
    //另一种注入方式
    private val historyDao by KoinJavaComponent.inject(HistoryDao::class.java)
    suspend fun register(username: String, password: String, repass: String) =
        withContext(Dispatchers.IO) {
            api.register(username, password, repass)
        }

    suspend fun insertHistory(history: History) =
        withContext(Dispatchers.IO) {
            historyDao.insertHistory(history)
        }

    suspend fun getHistory(user_id: String): History =
        withContext(Dispatchers.IO) { historyDao.queryHistorys(user_id) }
}