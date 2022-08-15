package com.it.openmechanic.di

import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.it.openmechanic.db.AppDataBase
import com.it.openmechanic.ui.activities.auth.AuthRepository
import com.it.openmechanic.ui.activities.auth.AuthViewModel
import com.it.openmechanic.ui.profile.ProfileRepository
import com.it.openmechanic.ui.profile.ProfileRepositoryImpl
import com.it.openmechanic.ui.profile.ProfileViewModel
import com.it.openmechanic.ui.publish.PublishRepository
import com.it.openmechanic.ui.publish.PublishRepositoryImpl
import com.it.openmechanic.utils.ADVERTISEMENTS
import com.it.openmechanic.utils.DEFAULT_DB_NAME
import com.it.openmechanic.utils.IMAGES
import com.it.openmechanic.utils.PROFILES
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single<ProfileRepository> { ProfileRepositoryImpl(profileRef = provideProfilesRef(), get()) }
    single<PublishRepository> {
        PublishRepositoryImpl(
            adRef = provideAdvertisementRef(),
            storageRef = provideFirebaseStorageRef()
        )
    }

    single {
        Room.databaseBuilder(
            androidContext(),
            AppDataBase::class.java,
            DEFAULT_DB_NAME
        ).allowMainThreadQueries().build()
    }

    single { AuthRepository(get()) }

    // FireBase authentication instance
    single { FirebaseAuth.getInstance() }


    viewModel { ProfileViewModel(get(), get()) }
    viewModel { AuthViewModel(get(), get(), get()) }
}

fun provideProfilesRef(): CollectionReference {
    return FirebaseFirestore.getInstance().collection(PROFILES)
}

fun provideAdvertisementRef(): CollectionReference {
    return FirebaseFirestore.getInstance().collection(ADVERTISEMENTS)
}

fun provideFirebaseStorageRef(): StorageReference {
    return FirebaseStorage.getInstance().reference.child(IMAGES)
}
