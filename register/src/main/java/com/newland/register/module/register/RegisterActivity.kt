package com.newland.register.module.register

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import com.newland.core.di.base.BaseActivity
import com.newland.core.extensions.showToast
import com.newland.register.R
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterActivity : BaseActivity() {
    private val messageTv by lazy { findViewById<AppCompatTextView>(R.id.tv_message) }
    private val mViewModel by viewModel<RegisterViewModel>()
    override fun getLayoutId(): Int = R.layout.activity_register
    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel.register("sdf", "sdf", "sdf")
        mViewModel.message.observe(this, {
            messageTv.setText(it)
            Log.e("mesa=====", it)
        })
        mViewModel.historyData.observe(this,{
            showToast(it.title)
        })
        findViewById<View>(R.id.btn_add).setOnClickListener({
            mViewModel.addHistory()
        })
        findViewById<View>(R.id.btn_query).setOnClickListener({
            mViewModel.getHistory()
        })
    }
}