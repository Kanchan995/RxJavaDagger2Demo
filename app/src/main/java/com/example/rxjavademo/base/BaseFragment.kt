package com.example.rxjavademo.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import dagger.android.support.DaggerFragment


abstract class BaseFragment : DaggerFragment() {

    private var activity: AppCompatActivity? = null

    @LayoutRes
    protected abstract fun layoutRes(): Int

    override fun onCreateView(
        @NonNull inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(layoutRes(), container, false)
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity=context as AppCompatActivity
    }

    override fun onDetach() {
        super.onDetach()
        activity = null
    }

    open fun getBaseActivity(): AppCompatActivity? {
        return activity
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }


}