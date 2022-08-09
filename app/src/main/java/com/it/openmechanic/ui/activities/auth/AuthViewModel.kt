package com.it.openmechanic.ui.activities.auth

import android.app.Activity
import android.app.Application
import android.util.Log
import android.util.LogPrinter
import androidx.lifecycle.LiveData
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.it.openmechanic.R
import com.it.openmechanic.data.Failure
import com.it.openmechanic.data.Success
import com.it.openmechanic.data.model.User
import com.it.openmechanic.ui.base.BaseViewModel
import com.it.openmechanic.ui.profile.ProfileRepository
import com.it.openmechanic.utils.*
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit


class AuthViewModel(
    app: Application,
    private val auth: FirebaseAuth,
    private val profileRepository: ProfileRepository
) : BaseViewModel(app) {

    init {
        auth.setLanguageCode("ru")
    }

    var isCodeSend = false
    var storedVerificationId = ""
    var resendToken: ForceResendingToken? = null


    val codeSendObservable by lazy { SingleLiveEvent<Boolean>() }
    val secondsObservable by lazy { SingleLiveEvent<String?>() }
    val smsErrorObservable by lazy { SingleLiveEvent<String?>() }
    val resendEnabledObservable by lazy { SingleLiveEvent<Boolean>() }

    val onVerificationFailed by lazy { SingleLiveEvent<String>() }
    val proceedRegistration by lazy { SingleLiveEvent<User>() }

    val isSuccessLogin by lazy { SingleLiveEvent<Pair<Boolean, String?>>() }

    private var secondsToGetNewCode = 60L
    private var resendTimer: Job? = null


    private val authCallback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            Log.e("TAG", "onVerificationCompleted:$credential")
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            Log.e("TAG", "onVerificationFailed", e)
            hideProgress()

            when (e) {
                is FirebaseAuthInvalidCredentialsException ->
                    onVerificationFailed.postValue(context.getString(R.string.invalid_credentials))

                is FirebaseTooManyRequestsException ->
                    onVerificationFailed.postValue(context.getString(R.string.too_many_requests))
            }
        }

        override fun onCodeSent(
            verificationId: String,
            token: ForceResendingToken
        ) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            Log.e("TAG", "onCodeSent:$verificationId")

            hideProgress()
            isCodeSend = true
            startSmsResendTimer()
            codeSendObservable.postValue(true)
            // Save verification ID and resending token so we can use them later
            storedVerificationId = verificationId
            resendToken = token
        }
    }

    private fun startSmsResendTimer() {
        resendTimer = CoroutineScope(Dispatchers.IO).async {
            while (isActive) {
                secondsToGetNewCode--
                delay(1000)
                secondsObservable.postValue(msTimeFormatter(getCurrentMillis(secondsToGetNewCode)))
                if (secondsToGetNewCode == 0L) {
                    resendEnabledObservable.postValue(true)
                    secondsToGetNewCode = 60L
                    cancel()
                }
            }
        }
    }

    private fun cancelResendSmsCodeTimer() {
        resendTimer?.cancel()
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.e("TAG", "signInWithCredential:success")

                    val uId = task.result?.user?.uid
                    if (uId != null) {
                        syncWithDataBase(uId)
                    } else Log.e("FireStore result", "user id that we got is null")
                } else {
                    hideProgress()
                    // Sign in failed, display a message and update the UI
                    Log.e("TAG", "signInWithCredential:failure", task.exception)
                    when ((task.exception as FirebaseAuthInvalidCredentialsException).errorCode) {

                        SMS_INVALID_CODE_ERROR -> {
                            smsErrorObservable.postValue(SMS_INVALID_CODE_ERROR)
                        }
                        SMS_EXPIRED_ERROR -> {
                            smsErrorObservable.postValue(SMS_EXPIRED_ERROR)
                        }
                    }
                    smsErrorObservable.postValue("The verification code entered was invalid")
                }
            }
    }

    private fun syncWithDataBase(uId: String) {
        launch {
            when (val result = profileRepository.isUserExists(uId)) {
                is Success -> {
                    if (result.data) {
                        isSuccessLogin.postValue(Pair(true, null))
                    } else {
                        proceedRegistration.postValue(User(id = uId))
                    }
                }
                is Failure -> {
                    isSuccessLogin.postValue(Pair(false, result.error.toString()))
                }
            }
            hideProgress()
        }
    }

    fun verifyPhoneNumber(phone: String, activity: AuthActivity) {
        showProgress()
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phone)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(authCallback)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun resendVerificationCode(
        phone: String,
        activity: Activity
    ) {
        showProgress()
        val options = resendToken?.let {
            PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(phone)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(activity)
                .setForceResendingToken(it)
                .setCallbacks(authCallback)
                .build()
        }
        if (options != null) {
            PhoneAuthProvider.verifyPhoneNumber(options)
        } else {
            smsErrorObservable.postValue("TOKEN IS NULL")
        }
    }

    fun confirmCode(code: String) {
        showProgress()
        cancelResendSmsCodeTimer()
        val credential = PhoneAuthProvider.getCredential(storedVerificationId, code)
        signInWithPhoneAuthCredential(credential)
    }

    fun reset() {
        isCodeSend = false
        resendTimer?.cancel()
        secondsToGetNewCode = 60L
    }
}