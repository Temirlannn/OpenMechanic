package com.it.openmechanic.ui.publish

import android.app.Application
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.it.openmechanic.R
import com.it.openmechanic.data.model.Advertisement
import com.it.openmechanic.data.model.Response
import com.it.openmechanic.data.model.Response.Error
import com.it.openmechanic.data.model.Response.Success
import com.it.openmechanic.ui.base.BaseViewModel
import com.it.openmechanic.ui.profile.ProfileRepository
import com.it.openmechanic.utils.SingleLiveEvent
import com.it.openmechanic.utils.getFileName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PublishViewModel(
    app: Application,
    private val repository: PublishRepository,
    private val profileRepo: ProfileRepository
) :
    BaseViewModel(app) {

    var addAdvertisementResponse = MutableLiveData<Response<Void?>>(Success(null))
        private set

    val storeImageResponse by lazy { SingleLiveEvent<Response<Uri>?>() }

    fun addAdvertisement(advertisement: Advertisement) = viewModelScope.launch {
        if (isNetworkAvailable) {
            val profile = getCurrentUser()?.uid?.let { profileRepo.getLocalProfile(it) }
            profile?.collect {
                advertisement.phoneNumber = it.phoneNumber.toString()
                advertisement.publisherName = it.userName.toString()
            }
            withContext(Dispatchers.IO) {
                repository.addAdvertisement(advertisement).collect {
                    addAdvertisementResponse.postValue(it)
                }
            }
        } else addAdvertisementResponse.postValue(Error(context.getString(R.string.no_internet)))
    }

    fun storeImages(uris: ArrayList<Uri>) = viewModelScope.launch {
        if (isNetworkAvailable) {
            uris.forEach { uri ->
                val fileName = uri.getFileName(context) ?: return@forEach
                withContext(Dispatchers.IO) {
                    repository.storeImage(fileName, uri).collect {
                        storeImageResponse.postValue(it)
                    }
                }
            }
            storeImageResponse.postValue(null)
        }
    }
}