package com.practice.branchinternapp.util

import android.content.Context
import com.practice.branchinternapp.util.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AuthTokenManager @Inject constructor(@ApplicationContext context: Context) {
    private var prefs =
        context.getSharedPreferences(Constants.PREFS_TOKEN_FILE, Context.MODE_PRIVATE)

    fun saveToken(token: String?) {
        val editor = prefs.edit()
        editor.putString(Constants.USER_TOKEN, token)
        editor.apply()
    }

    fun getToken(): String? {
        return prefs.getString(Constants.USER_TOKEN, null)

    }
}