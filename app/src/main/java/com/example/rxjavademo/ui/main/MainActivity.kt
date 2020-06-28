package com.example.rxjavademo.ui.main

import android.os.Bundle
import com.example.rxjavademo.R
import com.example.rxjavademo.base.BaseActivity
import com.example.rxjavademo.ui.list.NewsListFragment

class MainActivity : BaseActivity() {

    override fun layoutRes(): Int {
        return R.layout.activity_main
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            if (savedInstanceState == null)
                supportFragmentManager.beginTransaction()
                .add(R.id.container, NewsListFragment()).commit()
        }
    }
}