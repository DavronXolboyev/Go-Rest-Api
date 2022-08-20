package com.xdmobile.gorestapi.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Davron Xolboyev on 19.08.2022
 */
data class User(
    @SerializedName("id")
    val id :Int,
    @SerializedName("name")
    var name : String,
    @SerializedName("email")
    var email : String,
    @SerializedName("gender")
    val gender : String,
    @SerializedName("status")
    var status : String
)
