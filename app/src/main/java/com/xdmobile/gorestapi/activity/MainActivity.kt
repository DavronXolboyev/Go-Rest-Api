package com.xdmobile.gorestapi.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.xdmobile.gorestapi.R
import com.xdmobile.gorestapi.adapter.RvAdapter
import com.xdmobile.gorestapi.databinding.ActivityMainBinding
import com.xdmobile.gorestapi.databinding.DialogCreateUserBinding
import com.xdmobile.gorestapi.model.NewUser
import com.xdmobile.gorestapi.model.User
import com.xdmobile.gorestapi.model.UserUpdate
import com.xdmobile.gorestapi.utils.Resource

class MainActivity : AppCompatActivity() {

    private var adapter: RvAdapter? = null
    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding

    private lateinit var errorMessage: String
    private lateinit var loadingMessage: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        errorMessage = resources.getText(R.string.error_message).toString()
        loadingMessage = resources.getText(R.string.loading_message).toString()

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        binding.initData()
    }

    private fun ActivityMainBinding.initData() {

        adapter = RvAdapter { user, view ->
            val popupMenu = PopupMenu(this@MainActivity, view)
            val inflater = MenuInflater(this@MainActivity)
            inflater.inflate(R.menu.popup_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { item ->
                if (item?.itemId == R.id.popup_edit) {
                    showEditDialog(user)
                } else if (item?.itemId == R.id.popup_delete) {
                    deleteUser(user.id)
                }
                return@setOnMenuItemClickListener true
            }
            popupMenu.setForceShowIcon(true)
            popupMenu.show()
        }

        val indicator = DividerItemDecoration(this@MainActivity, LinearLayoutManager.VERTICAL)
        rvUsers.addItemDecoration(indicator)
        rvUsers.adapter = adapter

        viewModel.getUsers()
        setDataToRv()

        retryBtn.setOnClickListener {
            viewModel.getUsers()
        }

        searchBtn.setOnClickListener {
            if (editQuery.text.toString().trim().isNotEmpty()) {
                adapter?.clearData()
                viewModel.searchUser(editQuery.text.toString())
                viewModel.observeSearchedUsers().observe(this@MainActivity) {
                    when (it) {
                        is Resource.Success -> adapter?.setData(it.data!!)
                        is Resource.Error -> showToast(errorMessage)
                        is Resource.Empty -> showToast(loadingMessage, false)
                    }
                }
            }
        }
    }

    private fun showEditDialog(user: User) {
        val viewBinding = DialogCreateUserBinding.inflate(LayoutInflater.from(this@MainActivity))
        val dialogBuilder = AlertDialog.Builder(this)
            .setView(viewBinding.root)
        val dialog = dialogBuilder.create()

        with(viewBinding) {
            genderTv.visibility = View.GONE
            genderRadioGroup.visibility = View.GONE
            dialogCreateBtn.text = resources.getText(R.string.edit)
            dialogUserEmail.setText(user.email)
            dialogUsername.setText(user.name)
            if (user.status == "active")
                radioButtonActive.isChecked = true
            else
                radioButtonNotActive.isChecked = true
            dialogCreateBtn.setOnClickListener {
                if (dialogUsername.text.toString().trim()
                        .isNotEmpty() && dialogUserEmail.text.toString().trim()
                        .isNotEmpty()
                ) {
                    val status = if (radioButtonActive.isChecked) "active" else "inactive"
                    val newUser = UserUpdate(
                        name = dialogUsername.text.toString(),
                        email = dialogUserEmail.text.toString(),
                        status = status
                    )
                    updateUser(newUser, user.id)
                } else {
                    showToast("Fill in the blanks")
                }
                dialog.dismiss()
            }

        }

        dialog.show()
    }

    private fun updateUser(newUser: UserUpdate, userId: Int) {
        viewModel.updateUser(newUser, userId)
        viewModel.observeUserUpdate().observe(this) {
            when (it) {
                is Resource.Success -> {
                    if (it.data!!) {
                        adapter?.updateUser(newUser, userId)
                        showToast("User is updated")
                    }
                }
                is Resource.Error -> showToast(errorMessage)
                is Resource.Empty -> showToast(loadingMessage, false)
            }
        }
    }

    private fun deleteUser(userId: Int) {
        viewModel.deleteUser(userId)
        viewModel.observeUserDelete().observe(this) {
            when (it) {
                is Resource.Success -> {
                    viewModel.getUsers()
                    showToast("User is deleted")
                }
                is Resource.Error -> showToast(errorMessage)
                is Resource.Empty -> showToast(loadingMessage, false)
            }
        }
    }

    private fun setDataToRv() {
        viewModel.observeUser().observe(this@MainActivity) {
            when (it) {
                is Resource.Success -> {
                    adapter?.setData(it.data!!)
                    setVisibilities(false)
                }
                is Resource.Error -> {
                    setVisibilities(true)
                }
                is Resource.Empty -> {
                    setVisibilities(true)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        MenuInflater(this@MainActivity).apply {
            inflate(R.menu.menu_toolbar, menu)
        }
        menu.getItem(0).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_add_user) {
            showUserCreateDialog()
            return true
        } else if (item.itemId == R.id.menu_all_users) {
            viewModel.getUsers()
            viewModel.observeUser().observe(this) {
                when (it) {

                    is Resource.Success -> {
                        adapter?.setData(it.data!!)
                        setVisibilities(false)
                    }
                    is Resource.Error -> {
                        setVisibilities(true)
                    }
                    is Resource.Empty -> {
                        setVisibilities(true)
                    }
                }
            }
        }
        return true
    }

    private fun showUserCreateDialog() {
        val viewBinding = DialogCreateUserBinding.inflate(LayoutInflater.from(this@MainActivity))
        val dialogBuilder = AlertDialog.Builder(this)
            .setView(viewBinding.root)
        val dialog = dialogBuilder.create()
        with(viewBinding) {
            radioButtonMale.isChecked = true
            radioButtonActive.isChecked = true
            dialogCreateBtn.text = resources.getText(R.string.create)

            dialogCreateBtn.setOnClickListener {
                if (dialogUsername.text.toString().trim()
                        .isNotEmpty() && dialogUserEmail.text.toString().trim()
                        .isNotEmpty()
                ) {
                    val gender = if (radioButtonMale.isChecked) "male" else "female"
                    val status = if (radioButtonActive.isChecked) "active" else "inactive"
                    val newUser = NewUser(
                        name = dialogUsername.text.toString(),
                        email = dialogUserEmail.text.toString(),
                        gender = gender,
                        status = status
                    )
                    createUser(newUser)
                } else {
                    showToast("Fill in the blanks")
                }
                dialog.dismiss()
            }
        }

        dialog.show()
    }

    private fun createUser(newUser: NewUser) {
        viewModel.createUser(newUser)
        viewModel.observeCreateUser().observe(this) {
            when (it) {
                is Resource.Success -> {
                    if (it.data!!) {
                        showToast("User is created")
                        viewModel.getUsers()
                    }
                }
                is Resource.Error -> {
                    showToast("Something went wrong..")
                }
                is Resource.Empty -> showToast(loadingMessage, false)
            }
        }
    }

    private fun showToast(message: String, isLong: Boolean = true) {
        Toast.makeText(
            this@MainActivity,
            message,
            if (isLong)
                Toast.LENGTH_LONG
            else
                Toast.LENGTH_SHORT
        )
            .show()
    }

    private fun setVisibilities(isVisible: Boolean) {
        with(binding) {
            errorTv.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
            retryBtn.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
        }
    }

}
