package com.it.openmechanic.ui.profile

import com.google.firebase.firestore.CollectionReference
import com.it.openmechanic.data.model.Profile
import com.it.openmechanic.data.model.Response.*
import com.it.openmechanic.db.AppDataBase
import com.it.openmechanic.ui.base.BaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class ProfileRepositoryImpl(
    private val profileRef: CollectionReference,
    private val db: AppDataBase
) :
    BaseRepository(profileRef), ProfileRepository {

    override suspend fun addProfile(profile: Profile) = flow {
        try {
            emit(Loading)

            val id = profile.id
            val addition = profileRef.document(id).set(profile).await()
            emit(Success(addition))

        } catch (e: Exception) {
            emit(Error(e.message ?: e.toString()))
        }
    }

    override suspend fun getProfile(id: String) = flow {
        try {
            emit(Loading)
            val profile = profileRef.document(id).get().await().toObject(Profile::class.java)

            if (profile != null) {
                emit(Success(profile))
            } else {
                emit(Error("Profile is null"))
            }
        } catch (e: Exception) {
            emit(Error(e.message ?: e.toString()))
        }
    }

    override suspend fun isProfileExists(id: String) = flow {
        try {
            emit(Loading)
            emit(Success(profileRef.document(id).get().await().exists()))
        } catch (e: Exception) {
            emit(Error(e.message ?: e.toString()))
        }
    }

    override fun getLocalProfile(id: String): Flow<Profile> = flow {
        db.getProfileDao().getProfile(id).collect()
    }

    override suspend fun addLocalProfile(profile: Profile) = flow {
        try {
            val result = db.getProfileDao().saveProfile(profile)
            if (result == 1L) {
                emit(Success(result))
            } else emit(Error("Row is not saved"))
        } catch (e: Exception) {
            emit(Error(e.message ?: e.toString()))
        }
    }
}