package com.example.rxjavademo.ui.main

import com.example.rxjavademo.ui.detail.NewsDetailFragment
import com.example.rxjavademo.ui.list.NewsListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
public abstract class MainFragmentBindingModule {
    @ContributesAndroidInjector
    abstract fun provideListFragment(): NewsListFragment

    @ContributesAndroidInjector
    abstract fun provideDetailsFragment(): NewsDetailFragment
}