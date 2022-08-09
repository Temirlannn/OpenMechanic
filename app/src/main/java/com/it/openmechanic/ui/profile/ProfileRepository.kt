package com.it.openmechanic.ui.profile

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.it.openmechanic.data.Failure
import com.it.openmechanic.data.Result
import com.it.openmechanic.data.Success
import com.it.openmechanic.data.model.User
import com.it.openmechanic.enums.CollectionType
import kotlinx.coroutines.tasks.await

class ProfileRepository(private val fireStore: FirebaseFirestore) {
    suspend fun insertUser(user: User): Result<Boolean, Exception> {
        return try {
            Log.e("user: ", user.toString())
            fireStore
                .collection(CollectionType.USERS.code)
                .document(user.id.toString())
                .set(user)
                .await()
            Success(true)
        } catch (e: Exception) {
            Log.e("failed", e.message.toString())
            Failure(e)
        }
    }

    suspend fun getUser(id: String): Result<User, Exception> {
        return try {
            Log.e("id", id)
            val user =
                fireStore
                    .collection(CollectionType.USERS.code)
                    .document(id)
                    .get()
                    .await()
                    .toObject(User::class.java)

            if (user != null) Success(user)
            else Failure(Exception("No data"))

        } catch (e: Exception) {
            Failure(e)
        }
    }

    suspend fun isUserExists(id: String): Result<Boolean, Exception> {
        return try {
            val result = fireStore.collection(CollectionType.USERS.code)
                .document(id)
                .get().await()
            if (result.exists()) Success(true)
            else Success(false)

        } catch (e: Exception) {
            Failure(e)
        }
    }
}