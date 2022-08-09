package com.it.openmechanic.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.it.openmechanic.ui.activities.auth.AuthRepository
import com.it.openmechanic.ui.activities.auth.AuthViewModel
import com.it.openmechanic.ui.profile.ProfileRepository
import com.it.openmechanic.ui.profile.ProfileViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single { ProfileRepository(get()) }
    single { AuthRepository(get(), get()) }

    single { FirebaseFirestore.getInstance() }
    single { FirebaseAuth.getInstance() }


    viewModel { ProfileViewModel(get(), get()) }
    viewModel { AuthViewModel(get(), get(), get()) }
}
