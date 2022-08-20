package com.xdmobile.gorestapi.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xdmobile.gorestapi.databinding.ItemListBinding
import com.xdmobile.gorestapi.model.User
import com.xdmobile.gorestapi.model.UserUpdate

/**
 * Created by Davron Xolboyev on 19.08.2022
 */
class RvAdapter(private val onItemClick: (User, View) -> Unit) :
    RecyclerView.Adapter<RvAdapter.ViewHolder>() {

    private val userList: MutableList<User> = mutableListOf()

    fun setData(data: List<User>) {
        userList.clear()
        userList.addAll(data)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            with(binding) {
                username.text = userList[position].name
                userEmail.text = userList[position].email
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemListBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
        holder.itemView.setOnLongClickListener {
            onItemClick(userList[position], holder.itemView)
            return@setOnLongClickListener true
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    fun updateUser(newUser: UserUpdate, id: Int) {
        for (index in 0..userList.lastIndex) {
            if (userList[index].id == id) {
                with(userList[index]) {
                    name = newUser.name
                    email = newUser.email
                    status = newUser.status
                }
                notifyItemChanged(index)
                break
            }
        }
    }

    fun clearData(){
        userList.clear()
        notifyDataSetChanged()
    }

}