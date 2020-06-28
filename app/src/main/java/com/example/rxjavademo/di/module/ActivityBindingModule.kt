package com.example.rxjavademo.di.module

import com.example.rxjavademo.ui.main.MainActivity
import com.example.rxjavademo.ui.main.MainFragmentBindingModule
import dagger.Module

import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {
    @ContributesAndroidInjector(modules = [MainFragmentBindingModule::class])
    abstract fun bindMainActivity(): MainActivity
}