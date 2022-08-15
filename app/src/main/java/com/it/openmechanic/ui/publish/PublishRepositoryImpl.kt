package com.it.openmechanic.ui.publish

import android.net.Uri
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.storage.StorageReference
import com.it.openmechanic.data.model.Advertisement
import com.it.openmechanic.data.model.Response.*
import com.it.openmechanic.ui.base.BaseRepository
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class PublishRepositoryImpl(
    private val adRef: CollectionReference,
    private val storageRef: StorageReference
) : PublishRepository,
    BaseRepository(adRef) {

    override suspend fun addAdvertisement(advertisement: Advertisement) = flow {
        try {
            emit(Loading)
            advertisement.id = generateId()
            val addition = adRef.document(advertisement.id).set(advertisement).await()
            emit(Success(addition))
        } catch (e: Exception) {
            emit(Error(e.message ?: e.toString()))
        }
    }

    override suspend fun storeImage(fileName: String, uri: Uri) = flow {
        try {
            emit(Loading)
            val fileToUpload = storageRef.child(fileName)
            val storing = fileToUpload.putFile(uri).await()
            val downloadedUrl = storing.storage.downloadUrl.await()
            emit(Success(downloadedUrl))

        } catch (e: Exception) {
            emit(Error(e.message ?: e.toString()))
        }
    }
}