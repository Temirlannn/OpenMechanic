package com.it.openmechanic.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.it.openmechanic.data.model.Profile
import kotlinx.coroutines.flow.Flow

@Dao
interface ProfileDao {

    @Query("SELECT * FROM PROFILE WHERE id = :id")
    fun getProfile(id: String): Flow<Profile>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveProfile(profile: Profile): Long


//    @Query("SELECT * FROM PROFILE WHERE isAdditional = 1 LIMIT 1")
//
//
//    @Update
//    suspend fun updateProfile(profile: Profile)
//
//    @Query("SELECT * FROM PROFILE where isAdditional =0 LIMIT 1")
//    fun getPrimaryProfile(): Profile?

//    fun getAdditionalProfile(): Profile?
//

//
//    @Query("DELETE FROM PROFILE WHERE isAdditional = 0")
//    suspend fun deletePrimaryProfiles()
//
//    @Query("DELETE FROM PROFILE WHERE isAdditional = 1")
//    suspend fun deleteAdditionalProfiles()
//
//    @Query("DELETE FROM PROFILE WHERE profile_id = :id")
//    suspend fun deleteProfileById(id: String)
//
//    @Query("SELECT CASE WHEN EXISTS(SELECT *FROM PROFILE WHERE profile_id = :id) THEN 'TRUE' ELSE 'FALSE' END")
//    suspend fun isProfileExists(id: String): Boolean
}
