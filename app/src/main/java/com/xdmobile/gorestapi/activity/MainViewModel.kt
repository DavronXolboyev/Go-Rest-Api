package com.xdmobile.gorestapi.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.xdmobile.gorestapi.model.User
import com.xdmobile.gorestapi.model.NewUser
import com.xdmobile.gorestapi.model.UserUpdate
import com.xdmobile.gorestapi.repository.Repository
import com.xdmobile.gorestapi.utils.Resource

/**
 * Created by Davron Xolboyev on 19.08.2022
 */
class MainViewModel : ViewModel() {
    private var userList: MutableLiveData<Resource<List<User>>> = MutableLiveData()
    private var searchedUserList: MutableLiveData<Resource<List<User>>> = MutableLiveData()
    private var isUserCreated: MutableLiveData<Resource<Boolean>> = MutableLiveData()
    private var isUserUpdated: MutableLiveData<Resource<Boolean>> = MutableLiveData()
    private var isUserDeleted: MutableLiveData<Resource<Boolean>> = MutableLiveData()
    private val repository = Repository()

    fun observeUser(): LiveData<Resource<List<User>>> {
        return userList
    }

    fun getUsers() {
        userList = repository.fetchUsers()
    }

    fun observeCreateUser(): LiveData<Resource<Boolean>> {
        return isUserCreated
    }

    fun createUser(newUser: NewUser) {
        isUserCreated = repository.createUser(newUser)
    }

    fun observeUserDelete(): LiveData<Resource<Boolean>> {
        return isUserDeleted
    }

    fun deleteUser(id: Int) {
        isUserDeleted = repository.deleteUser(id)
    }

    fun observeUserUpdate(): LiveData<Resource<Boolean>> {
        return isUserUpdated
    }

    fun updateUser(newUser: UserUpdate, id: Int) {
        isUserUpdated = repository.updateUser(newUser, id)
    }

    fun searchUser(name: String) {
        searchedUserList = repository.searchUsers(name)
    }

    fun observeSearchedUsers(): LiveData<Resource<List<User>>> {
        return searchedUserList
    }
}