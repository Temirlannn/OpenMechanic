package com.it.openmechanic.ui.profile

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.it.openmechanic.R
import com.it.openmechanic.data.model.Response.*
import com.it.openmechanic.data.model.Profile
import com.it.openmechanic.data.model.Response
import com.it.openmechanic.ui.base.BaseViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ProfileViewModel(app: Application, private val repository: ProfileRepositoryImpl) :
    BaseViewModel(app) {

    var addProfileResponse = MutableLiveData<Response<Void?>>(Success(null))
        private set

    var getProfileResponse = MutableLiveData<Response<Profile>>()
        private set

    fun addProfile(profile: Profile) = viewModelScope.launch {
        if (isNetworkAvailable) {
            profile.id = getCurrentUser()?.uid ?: ""
            repository.addProfile(profile).collect { response ->
                addProfileResponse.postValue(response)
            }
        } else addProfileResponse.postValue(Error(context.getString(R.string.no_internet)))
    }

    fun getProfile() = viewModelScope.launch {
        if (isNetworkAvailable) {
            val id = getCurrentUser()?.uid ?: ""
            repository.getProfile(id).collect {
                if (it is Success) repository.addLocalProfile(it.data)
                getProfileResponse.postValue(it)
            }
        } else getProfileResponse.postValue(Error(context.getString(R.string.no_internet)))
    }
}