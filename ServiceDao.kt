package com.rsdurvasacooling.service

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ServiceDao {
    @Insert suspend fun insert(record: ServiceRecord): Long
    @Query("SELECT * FROM service_records ORDER BY createdAt DESC")
    suspend fun all(): List<ServiceRecord>
    @Query("SELECT * FROM service_records WHERE synced = 0")
    suspend fun unsynced(): List<ServiceRecord>
    @Query("UPDATE service_records SET synced = 1 WHERE id = :id")
    suspend fun markSynced(id: Long)
}
