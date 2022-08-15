package com.it.openmechanic.ui.publish

import android.net.Uri
import com.it.openmechanic.data.model.Advertisement
import com.it.openmechanic.data.model.Response
import kotlinx.coroutines.flow.Flow

interface PublishRepository {

    suspend fun addAdvertisement(advertisement: Advertisement): Flow<Response<Void?>>

    suspend fun storeImage(fileName: String, uri: Uri): Flow<Response<Uri>>
}