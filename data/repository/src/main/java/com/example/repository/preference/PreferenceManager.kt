package com.example.repository.preference

import android.content.Context
import javax.inject.Inject

/**
 * Preference制御管理
 */
class PreferenceManager @Inject constructor(val context: Context) {

    companion object{
        private const val PREFERENCE_NAME = "shogi-preference"
    }
    /**
     * Int型格納
     *
     * @param key   キー
     * @param value 格納する値
     */
    fun setString(
        key: PreferenceKey.IntKey,
        value: Int
    ) {
        val preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putInt(key.name, value)
        editor.apply()
    }

    /**
     * Int型取得
     *
     * @param key キー
     * @return キーに紐づくInt型オブジェクト
     */
    fun getString(key: PreferenceKey.IntKey): Int {
        val defaultValue = 0
        val preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        return preferences.getInt(key.name, defaultValue)
    }

    /**
     * Int型格納
     *
     * @param key   キー
     * @param value 格納する値
     */
    fun setInt(
        key: PreferenceKey.IntKey,
        value: Int
    ) {
        val preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putInt(key.name, value)
        editor.apply()
    }

    /**
     * Int型取得
     *
     * @param key キー
     * @return キーに紐づくInt型オブジェクト
     */
    fun getInt(key: PreferenceKey.IntKey): Int {
        val defaultValue = 0
        val preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        return preferences.getInt(key.name, defaultValue)
    }

    /**
     * Long型格納
     *
     * @param key   キー
     * @param value 格納する値
     */
    fun setLong(
        key: PreferenceKey.LongKey,
        value: Long?
    ) {
        val preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putLong(key.name, value!!)
        editor.apply()
    }

    /**
     * Long型取得
     *
     * @param key キー
     * @return キーに紐づくLong型オブジェクト
     */
    fun getLong(key: PreferenceKey.LongKey): Long {
        val defaultValue = 0L
        val preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        return preferences.getLong(key.name, defaultValue)
    }
}