package com.example.etnthreepages.util

import android.content.Context
import android.content.SharedPreferences

class TokenAuth private constructor(context: Context) {
    companion object {
        private const val TOKEN = "TOKEN"
        private const val TOKEN_VALUE = "TOKEN_VALUE"

        @Volatile
        private var instance: TokenAuth? = null

        fun getInstance(context: Context): TokenAuth = instance?: synchronized(this){ TokenAuth(context).apply  { instance = this }}
    }

    private val sharedPreference: SharedPreferences = context.getSharedPreferences(TOKEN, Context.MODE_PRIVATE)

    var token: String? = null
        set(value) = sharedPreference.edit().putString(TOKEN_VALUE, value).apply().also { field =value }
        get() = field ?: sharedPreference.getString(TOKEN_VALUE, null)
}