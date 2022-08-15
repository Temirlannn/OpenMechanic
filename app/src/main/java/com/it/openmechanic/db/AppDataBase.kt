package com.it.openmechanic.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.it.openmechanic.data.model.Profile
import com.it.openmechanic.db.dao.ProfileDao

@Database(
    entities = [
        Profile::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDataBase : RoomDatabase() {
    abstract fun getProfileDao(): ProfileDao
}