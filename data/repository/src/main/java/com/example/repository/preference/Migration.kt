package com.example.repository.preference

import javax.inject.Inject


/**
 * マイグレーション
 *
 */
class Migration @Inject constructor(
    private val preferenceManager: PreferenceManager,
    private val encryptedPreferenceManager: EncryptedPreferenceManager,
) {
    companion object {
        const val DIVER = ","
    }

    init {
        upgrade(PreferenceKey.migrationKeys)
    }

    private fun upgrade(migrationKeys: Map<PreferenceKey<Long>, PreferenceKey<Long>>) {
        val migrationKey = PreferenceKey.MIGRATION_COMPLETE_KEY(PreferenceKey.VERSION1)
        val migratedKeys = preferenceManager.get(migrationKey) ?: ""

        val migrationCompleteKey: MutableList<String> = migratedKeys.split(DIVER).toMutableList()
        migrationKeys.forEach { (oldVersionKey, newVersionKey) ->
            if (!migratedKeys.contains(oldVersionKey.rawKey)) {
                val oldData = preferenceManager.get(oldVersionKey)
                encryptedPreferenceManager.set(newVersionKey, oldData)
                migrationCompleteKey.add(oldVersionKey.rawKey)
            }
        }
        val jointData = migrationCompleteKey.joinToString(separator = DIVER)
        preferenceManager.set(migrationKey, jointData)
    }

    // TODO ダウングレード処理
    private fun downgrade(migrationKeys: Map<PreferenceKey<Long>, PreferenceKey<Long>>) {
        // マイグレーションしたキーを取得
        val migrationKey1 = PreferenceKey.MIGRATION_COMPLETE_KEY(PreferenceKey.VERSION1)
        val migrationKey2 = PreferenceKey.MIGRATION_COMPLETE_KEY(PreferenceKey.VERSION2)
        val migratedKeys = preferenceManager.get(migrationKey1) ?: ""

        val migrationCompleteKey: MutableList<String> = migratedKeys.split(DIVER).toMutableList()
        migrationKeys.forEach { (oldVersionKey, newVersionKey) ->
            if (!migratedKeys.contains(oldVersionKey.rawKey)) {
                val oldData = preferenceManager.get(oldVersionKey)
                preferenceManager.set(newVersionKey, oldData)
                migrationCompleteKey.add(oldVersionKey.rawKey)
            }
        }
        val jointData = migrationCompleteKey.joinToString(separator = DIVER)
        preferenceManager.set(migrationKey1, jointData)
    }
}
