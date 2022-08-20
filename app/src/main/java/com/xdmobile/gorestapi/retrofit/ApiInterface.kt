package com.xdmobile.gorestapi.retrofit

import com.xdmobile.gorestapi.model.User
import com.xdmobile.gorestapi.model.NewUser
import com.xdmobile.gorestapi.model.UserUpdate
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by Davron Xolboyev on 19.08.2022
 */
interface ApiInterface {
    @GET("users")
    @Headers(
        "Accept:application/json",
        "Content-Type:application/json",
        "Authorization: Bearer ac1af363f79e37a0c6230ed29b0fdab92b89edd1efd45edbef705a25a7f8530c"
    )
    fun getUserList(): Call<List<User>>

    @POST("users")
    @Headers(
        "Accept:application/json",
        "Content-Type:application/json",
        "Authorization: Bearer ac1af363f79e37a0c6230ed29b0fdab92b89edd1efd45edbef705a25a7f8530c"
    )
    fun putUser(@Body user: NewUser): Call<NewUser>

    @PATCH("users/{id}")
    @Headers(
        "Accept:application/json",
        "Content-Type:application/json",
        "Authorization: Bearer ac1af363f79e37a0c6230ed29b0fdab92b89edd1efd45edbef705a25a7f8530c"
    )
    fun updateUser(
        @Body user: UserUpdate,
        @Path("id") id: Int
    ): Call<User>

    @DELETE("users/{id}")
    @Headers(
        "Accept:application/json",
        "Content-Type:application/json",
        "Authorization: Bearer ac1af363f79e37a0c6230ed29b0fdab92b89edd1efd45edbef705a25a7f8530c"
    )
    fun deleteUser(@Path("id") userId: Int): Call<User>

    @GET("users")
    @Headers(
        "Accept:application/json",
        "Content-Type:application/json",
        "Authorization: Bearer ac1af363f79e37a0c6230ed29b0fdab92b89edd1efd45edbef705a25a7f8530c"
    )
    fun searchUsers(@Query("name") name: String): Call<List<User>>

}