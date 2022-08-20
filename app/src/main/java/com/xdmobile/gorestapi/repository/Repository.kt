package com.xdmobile.gorestapi.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.xdmobile.gorestapi.model.User
import com.xdmobile.gorestapi.model.NewUser
import com.xdmobile.gorestapi.model.UserUpdate
import com.xdmobile.gorestapi.retrofit.ApiClient
import com.xdmobile.gorestapi.retrofit.ApiInterface
import com.xdmobile.gorestapi.utils.Resource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Davron Xolboyev on 19.08.2022
 */
class Repository {

    private val userList = MutableLiveData<Resource<List<User>>>(Resource.Empty)
    private val searchedUserList = MutableLiveData<Resource<List<User>>>(Resource.Empty)
    private val isUserCreated: MutableLiveData<Resource<Boolean>> = MutableLiveData(Resource.Empty)
    private val isUserUpdated: MutableLiveData<Resource<Boolean>> = MutableLiveData(Resource.Empty)
    private val isUserDeleted: MutableLiveData<Resource<Boolean>> = MutableLiveData(Resource.Empty)

    fun fetchUsers(): MutableLiveData<Resource<List<User>>> {
        try {
            ApiClient.getInstance().create(ApiInterface::class.java)
                .getUserList().enqueue(object : Callback<List<User>> {
                    override fun onResponse(
                        call: Call<List<User>>,
                        response: Response<List<User>>
                    ) {
                        if (response.isSuccessful)
                            userList.postValue(Resource.Success(response.body()))
                    }

                    override fun onFailure(call: Call<List<User>>, t: Throwable) {
                        userList.postValue(Resource.Error(t.message!!))
                    }

                })
        } catch (e: Exception) {
            userList.postValue(Resource.Error(e.message!!))
        }

        return userList
    }

    fun createUser(newUser: NewUser): MutableLiveData<Resource<Boolean>> {
        try {
            ApiClient.getInstance().create(ApiInterface::class.java)
                .putUser(newUser)
                .enqueue(object : Callback<NewUser> {
                    override fun onResponse(call: Call<NewUser>, response: Response<NewUser>) {
                        if (response.isSuccessful)
                            isUserCreated.postValue(
                                Resource.Success(true)
                            )
                    }

                    override fun onFailure(call: Call<NewUser>, t: Throwable) {
                        isUserCreated.postValue(Resource.Error(t.message!!))
                    }

                })
        } catch (e: Exception) {
            isUserCreated.postValue(Resource.Error(e.message!!))
        }
        return isUserCreated
    }

    fun updateUser(newUser: UserUpdate, id: Int): MutableLiveData<Resource<Boolean>> {
        try {
            ApiClient.getInstance().create(ApiInterface::class.java)
                .updateUser(newUser, id)
                .enqueue(object : Callback<User> {
                    override fun onResponse(
                        call: Call<User>,
                        response: Response<User>
                    ) {
                        if (response.isSuccessful)
                            isUserUpdated.postValue(Resource.Success(true))
                    }

                    override fun onFailure(call: Call<User>, t: Throwable) {
                        isUserUpdated.postValue(Resource.Error(t.message!!))
                    }
                })

        } catch (e: Exception) {
            isUserUpdated.postValue(Resource.Error(e.message!!))
        }
        return isUserUpdated
    }

    fun deleteUser(id: Int): MutableLiveData<Resource<Boolean>> {
        try {
            ApiClient.getInstance().create(ApiInterface::class.java)
                .deleteUser(id)
                .enqueue(object : Callback<User> {
                    override fun onResponse(call: Call<User>, response: Response<User>) {
                        if (response.isSuccessful)
                            isUserDeleted.postValue(Resource.Success(true))
                    }

                    override fun onFailure(call: Call<User>, t: Throwable) {
                        Log.i("TTT", "onFailure: ${t.message!!}")
                        isUserDeleted.postValue(Resource.Error(t.message!!))
                    }
                })
        } catch (e: Exception) {
            Log.i("TTT", "deleteUser: ${e.message!!}")
            isUserDeleted.postValue(Resource.Error(e.message!!))
        }
        return isUserDeleted
    }

    fun searchUsers(name: String): MutableLiveData<Resource<List<User>>> {
        try {
            ApiClient.getInstance().create(ApiInterface::class.java)
                .searchUsers(name)
                .enqueue(object : Callback<List<User>> {
                    override fun onResponse(
                        call: Call<List<User>>,
                        response: Response<List<User>>
                    ) {
                        if (response.isSuccessful)
                            searchedUserList.postValue(Resource.Success(response.body()))
                    }

                    override fun onFailure(call: Call<List<User>>, t: Throwable) {
                        searchedUserList.postValue(Resource.Error(t.message!!))
                    }

                })
        } catch (e: Exception) {
            searchedUserList.postValue(Resource.Error(e.message!!))
        }
        return searchedUserList
    }
}