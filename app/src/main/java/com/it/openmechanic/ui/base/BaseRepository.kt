package com.it.openmechanic.ui.base

import com.google.firebase.firestore.CollectionReference

open class BaseRepository(private val collectionRef: CollectionReference) {

    protected fun generateId(): String {
        return collectionRef.document().id
    }
}