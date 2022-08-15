package com.it.openmechanic.ui.activities.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.it.openmechanic.ui.base.BaseRepository

class AuthRepository(private val auth: FirebaseAuth)