package com.xdmobile.gorestapi.utils

/**
 * Created by Davron Xolboyev on 19.08.2022
 */
sealed class Resource<out T> {
    data class Success<out T>(val data : T?) : Resource<T>()
    data class Error(val message : String) : Resource<Nothing>()
    object Empty : Resource<Nothing>()
}
