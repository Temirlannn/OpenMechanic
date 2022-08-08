package com.it.openmechanic.enums

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
internal enum class CollectionType(val code: String) : Parcelable {
    USERS("users"),
    NONE("none");

    companion object {
        private val DEFAULT = NONE
        fun findByCode(code: String) = values().find { it.code.contentEquals(code) } ?: DEFAULT
    }
}