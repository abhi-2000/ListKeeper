package com.groww.myapplication.data

import android.content.Context
import android.content.SharedPreferences

class MySharedPreferences(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    fun saveInt(key: String, value: Int) {
        editor.putInt(key, value)
        editor.apply()
    }

    fun getInt(key: String, defaultValue: Int): Int {
        return sharedPreferences.getInt(key, defaultValue)
    }

    fun clearPreferences() {
        editor.clear()
        editor.apply()
    }
}
