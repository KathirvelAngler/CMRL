package com.cmrl.customer.preference

import android.content.Context
import android.content.SharedPreferences
import com.cmrl.customer.R
import com.cmrl.customer.model.User
import com.google.gson.Gson

/**
 * Created by Mathan on 22/08/19.
 **/

object LocalStorageSP {

    private fun getEditor(c: Context): SharedPreferences.Editor {
        return getPreference(c).edit()
    }

    private fun getPreference(c: Context): SharedPreferences {
        return c.getSharedPreferences(
                c.getString(R.string.app_name), Context.MODE_PRIVATE
        )
    }

    /**
     * Store the login user data in stored preferences for further access. Consider like a session
     *
     * @param c Context of the app
     * @param u User Object
     */
    fun storeLoginUser(c: Context, u: User?) {
        val e = getEditor(c)
        if (u == null) {
            e.remove("user_info")
        } else {
            val gson = Gson()
            e.putString("user_info", gson.toJson(u))
        }
        e.commit()

    }

    /**
     * Get login user object from stored preferences
     *
     * @param c Context of the app
     * @return User instance if exists else nulls
     */
    fun getLoginUser(c: Context?): User {
        val s = getPreference(c!!).getString("user_info", "")
        if (s == null || s.isEmpty()) {
            return User()
        }
        return Gson().fromJson(s, User::class.java)
    }

    /**
     * @param c     Context
     * @param key   key
     * @param value value
     */
    fun put(c: Context, key: String, value: String) {
        val editor = getEditor(c)
        editor.putString(key, value)
        editor.commit()
    }

    /**
     * @param c   Context
     * @param key key
     * @param dv  default value
     */
    operator fun get(c: Context, key: String, dv: String): String? {
        return getPreference(c).getString(key, dv)
    }

    /**
     * @param c     Context
     * @param key   key
     * @param value value
     */
    fun put(c: Context, key: String, value: Boolean) {
        val editor = getEditor(c)
        editor.putBoolean(key, value)
        editor.commit()
    }

    /**
     * @param c   Context
     * @param key key
     * @param dv  default value
     */
    operator fun get(c: Context, key: String, dv: Boolean): Boolean? {
        return getPreference(c).getBoolean(key, dv)
    }

    fun clearAll(c: Context) {
        val editor = getEditor(c)
        editor.clear()
        editor.commit()
    }

    fun clear(c: Context, key: String) {
        val editor = getEditor(c)
        editor.remove(key)
        editor.commit()
    }
}
