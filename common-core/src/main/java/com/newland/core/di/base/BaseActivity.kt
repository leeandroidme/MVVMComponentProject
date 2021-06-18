package com.newland.core.di.base

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.newland.core.manager.AppManager

abstract class BaseActivity : AppCompatActivity() {
    lateinit var activity: Activity
    override fun onCreate(savedInstanceState: Bundle?) {
        AppManager.addActivity(this)
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        activity = this
        if (!initData()) {
            AppManager.finishActivity(this)
            return
        }
        initView(savedInstanceState)
        initLoad()
    }

    override fun onDestroy() {
        super.onDestroy()
        AppManager.removeActivity(this)
    }

    abstract fun getLayoutId(): Int
    open protected fun initData(): Boolean {
        return true
    }

    open protected fun initView(savedInstanceState: Bundle?) {}
    open protected fun initLoad() {}
}