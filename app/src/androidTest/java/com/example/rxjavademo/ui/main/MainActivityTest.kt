package com.example.rxjavademo.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.example.rxjavademo.ui.list.NewsListFragment
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


@RunWith(JUnit4::class)
class MainActivityTest {

    @get:Rule
    var activityScenarioRule =
        ActivityScenarioRule(
            MainActivity::class.java
        )
    var mainActivity: MainActivity? = null

    @Before
    fun setUp() {
        mainActivity = mainActivity
    }

    @Test
    public fun startFragment() {
        mainActivity?.let {
            val newsListFragment = NewsListFragment()
            val fragmentManager: FragmentManager = it.getSupportFragmentManager()
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.add(newsListFragment, null)
            fragmentTransaction.commit()
        }

    }
}
