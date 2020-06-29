package com.example.rxjavademo.utils

import android.content.Context

class SharedPref {

    companion object {

        val NEWS_RESPONSE=""
        fun storeString(context: Context, tagName: String, value: String) {
            val sharedPrefHelper =
                context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE).edit()
            sharedPrefHelper.putString(tagName, value)
            sharedPrefHelper.apply()
        }

        fun getStringValue(context: Context, tagName: String): String? {
            return context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE).getString(tagName,"")
        }
    }
}