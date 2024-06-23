package com.example.repository.preference

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import javax.inject.Inject


/**
 * Preference制御管理
 */
class EncryptedPreferenceManager @Inject constructor(context: Context) {

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()
    private val preferences = EncryptedSharedPreferences.create(
        context,
        PREFERENCE_NAME,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    /**
     * String型格納
     *
     * @param key   キー
     * @param value 格納する値
     */
    fun set(
        key: PreferenceKey<String>,
        value: String
    ) {
        preferences.edit()
            .putString(key.rawKey, value)
            .apply()
    }

    /**
     * String型取得
     *
     * @param key キー
     * @return キーに紐づくInt型オブジェクト
     */
    fun get(key: PreferenceKey<String>): String? {
        return preferences.getString(key.rawKey, null)
    }

    /**
     * Int型格納
     *
     * @param key   キー
     * @param value 格納する値
     */
    fun set(
        key: PreferenceKey<Int>,
        value: Int
    ) {
        preferences.edit()
            .putInt(key.rawKey, value)
            .apply()
    }

    /**
     * Int型取得
     *
     * @param key キー
     * @return キーに紐づくInt型オブジェクト
     */
    fun get(key: PreferenceKey<Int>): Int {
        val defaultValue = 0
        return preferences.getInt(key.rawKey, defaultValue)
    }

    /**
     * Long型格納
     *
     * @param key   キー
     * @param value 格納する値
     */
    fun set(
        key: PreferenceKey<Long>,
        value: Long
    ) {
        preferences.edit()
            .putLong(key.rawKey, value)
            .apply()
    }

    /**
     * Long型取得
     *
     * @param key キー
     * @return キーに紐づくLong型オブジェクト
     */
    fun get(key: PreferenceKey<Long>): Long {
        val defaultValue = 0L
        return preferences.getLong(key.rawKey, defaultValue)
    }

    companion object {
        private const val PREFERENCE_NAME = "shogi-encrypted-preference"
    }
}
