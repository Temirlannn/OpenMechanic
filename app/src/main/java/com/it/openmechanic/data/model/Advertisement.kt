package com.it.openmechanic.data.model

data class Advertisement(
    var id: String = "",
    var description: String = "",
    var address: String = "",
    var publisherName: String = "",
    var phoneNumber: String = "",
    val images: ArrayList<String> = arrayListOf(),
    var timeStamp: String = ""
)
