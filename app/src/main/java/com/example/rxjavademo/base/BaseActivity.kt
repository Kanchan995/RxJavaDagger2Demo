package com.example.rxjavademo.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import dagger.android.HasAndroidInjector
import dagger.android.support.DaggerAppCompatActivity


abstract class BaseActivity :DaggerAppCompatActivity() {
    @LayoutRes
    protected abstract fun layoutRes(): Int


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutRes());
    }
}