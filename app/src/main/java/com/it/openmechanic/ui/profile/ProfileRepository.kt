package com.it.openmechanic.ui.profile

import com.it.openmechanic.data.model.Profile
import com.it.openmechanic.data.model.Response
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {

    suspend fun addProfile(profile: Profile): Flow<Response<Void?>>

    suspend fun getProfile(id: String): Flow<Response<Profile>>

    suspend fun isProfileExists(id: String): Flow<Response<Boolean>>

    fun getLocalProfile(id: String): Flow<Profile>

    suspend fun addLocalProfile(profile: Profile): Flow<Response<Long>>
}