package com.rsdurvasacooling.service

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ServiceRecord::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun serviceDao(): ServiceDao
}
