package com.it.openmechanic.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity
data class Profile(

    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: String = "",

    @ColumnInfo(name = "user_name")
    val userName: String? = "",

    @ColumnInfo(name = "phone_number")
    val phoneNumber: String? = "",

    @ColumnInfo(name = "email")
    val email: String? = "",

    @ColumnInfo(name = "profile_image")
    val profileImage: String? = "",
) : Parcelable