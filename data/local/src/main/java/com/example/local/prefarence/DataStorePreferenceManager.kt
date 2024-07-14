package com.example.local.prefarence

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * DataStorePreference制御管理
 */
class DataStorePreferenceManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val scope: CoroutineScope,
) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCE_NAME)

    /**
     * データ購読
     *
     * @param T 購読する型
     * @param key キー
     * @return キーに紐づくデータの購読
     */
    suspend fun <T : Any> observe(key: DataStorePreferenceKey<T>): StateFlow<T> =
        context.dataStore
            .flow(key)
            .stateIn(scope, SharingStarted.Lazily, get(key))

    /**
     * データ保存
     *
     * @param T 格納する型
     * @param key キー
     * @param value 対象のキーに保存する値
     */
    suspend fun <T : Any> set(key: DataStorePreferenceKey<T>, value: T) {
        context.dataStore.edit { it[key.rawKey] = value }
    }

    /**
     * データ取得
     *
     * @param T 取得する型
     * @param key キー
     * @return キーに紐づくデータ
     */
    suspend fun <T : Any> get(key: DataStorePreferenceKey<T>): T =
        context.dataStore
            .flow(key)
            .first()

    private fun <T : Any> DataStore<Preferences>.flow(key: DataStorePreferenceKey<T>): Flow<T> =
        data.map { it[key.rawKey] ?: key.defaultValue }

    companion object {
        private const val PREFERENCE_NAME = "shogi-data-store-preference"
    }
}
