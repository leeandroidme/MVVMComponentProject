package com.newland.mvvm.module.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.newland.mvvm.R
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val mViewModel by viewModel<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}