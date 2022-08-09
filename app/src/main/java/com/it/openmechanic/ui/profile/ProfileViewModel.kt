package com.it.openmechanic.ui.profile

import android.app.Application
import com.it.openmechanic.R
import com.it.openmechanic.data.Failure
import com.it.openmechanic.data.Success
import com.it.openmechanic.data.model.User
import com.it.openmechanic.ui.base.BaseViewModel
import com.it.openmechanic.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileViewModel(app: Application, private val repository: ProfileRepository) :
    BaseViewModel(app) {

    val userInsertObservable by lazy { SingleLiveEvent<Boolean>() }
    val errorObservable by lazy { SingleLiveEvent<String>() }


    fun insertUser(user: User) {
        launch {
            if (isNetworkAvailable) {
                showProgress()
                when (val result = withContext(Dispatchers.IO) { repository.insertUser(user) }) {
                    is Success -> {
                        userInsertObservable.postValue(true)
                    }
                    is Failure -> {
                        errorObservable.postValue(result.error.toString())
                    }
                }
                hideProgress()
            } else {
                errorObservable.postValue(context.getString(R.string.no_internet))
            }
        }
    }
}