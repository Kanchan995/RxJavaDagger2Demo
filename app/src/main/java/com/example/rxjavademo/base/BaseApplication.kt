package com.example.rxjavademo.base

import com.example.rxjavademo.di.component.ApplicationComponent
import com.example.rxjavademo.di.component.DaggerApplicationComponent
import com.example.rxjavademo.utils.InternetUtil
import dagger.android.DaggerApplication

class BaseApplication : DaggerApplication() {

    override fun applicationInjector(): ApplicationComponent {
        val component: ApplicationComponent =
            DaggerApplicationComponent.builder().application(this).build()
        component.inject(this)
        InternetUtil.init(this)
        return component
    }

}