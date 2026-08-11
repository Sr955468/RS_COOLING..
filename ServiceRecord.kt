package com.rsdurvasacooling.service

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "service_records")
data class ServiceRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val customerName: String,
    val phone: String,
    val address: String,
    val service: String,
    val amount: Double,
    val createdAt: Long = System.currentTimeMillis(),
    val synced: Boolean = false
)
