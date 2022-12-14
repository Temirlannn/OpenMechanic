package com.it.openmechanic.ui.base

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.it.openmechanic.utils.isNetworkAvailable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel(context: Application) :
    AndroidViewModel(context),
    CoroutineScope,
    KoinComponent {
    val appContext: Context by inject()
    val sp: SharedPreferences by inject()

    internal val _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable> = _error
    private val _progress = MutableLiveData<Boolean>()
    val progress: LiveData<Boolean> = _progress

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    fun showProgress() {
        if (isNetworkAvailable) {
            _progress.postValue(true)
        }
    }

    val context: Context
        get() = getApplication()

    val isNetworkAvailable: Boolean
        get() = context.isNetworkAvailable()

    fun hideProgress() {
        _progress.postValue(false)
    }

    fun getCurrentUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }
}
