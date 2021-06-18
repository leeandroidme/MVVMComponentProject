package com.newland.register.module.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.newland.register.db.entity.History
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class RegisterViewModel(private val repository: RegisterRepository) : ViewModel() {
    val message: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val historyData: MutableLiveData<History> by lazy {
        MutableLiveData<History>()
    }

    fun register(username: String, password: String, repass: String) {
        viewModelScope.launch {
            flow {
                emit(repository.register(username, password, repass))
            }.catch {
                message.value = it.message
            }.onStart {
                message.value = "注册开始了。。。"
            }.onCompletion {
                message.value = "注册完成了。。。"
            }.collectLatest {
                message.value = it.message()
            }
        }
    }

    fun addHistory() {
        var history = History(0, "10000", 1, "http://xxxxx.com", "android历史纪录", ".....");
        viewModelScope.launch {
            flow {
                emit(repository.insertHistory(history))
            }.catch {
                message.value = "添加历史纪录出现问题了"
            }.collectLatest {
                message.value = "添加成功了"
            }
        }
    }

    fun getHistory() {
        viewModelScope.launch {
            flow {
                emit(repository.getHistory("10000"))
            }.catch {

            }.collectLatest {
                historyData.value = it
            }
        }
    }
}