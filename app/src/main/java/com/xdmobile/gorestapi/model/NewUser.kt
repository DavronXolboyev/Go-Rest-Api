package com.xdmobile.gorestapi.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Davron Xolboyev on 19.08.2022
 */
data class NewUser(
    @SerializedName("name")
    val name : String,
    @SerializedName("email")
    val email : String,
    @SerializedName("gender")
    val gender : String,
    @SerializedName("status")
    val status : String
)