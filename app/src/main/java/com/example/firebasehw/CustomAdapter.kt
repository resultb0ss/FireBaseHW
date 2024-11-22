package com.example.firebasehw

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasehw.databinding.ItemListViewBinding

class CustomAdapter(var users: MutableList<UserData>) :
    RecyclerView.Adapter<CustomAdapter.UserViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserViewHolder {

        val binding =
            ItemListViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun getItemCount() = users.size

    override fun onBindViewHolder(
        holder: UserViewHolder,
        position: Int
    ) {
        val user = users[position]
        holder.binding.itemListUserNameTV.text = user.name.toString()
        holder.binding.itemListUserPhoneTV.text = user.phone.toString()
    }

    class UserViewHolder(val binding: ItemListViewBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateAdapter(newList: MutableList<UserData>) {
        users = newList
        notifyDataSetChanged()

    }
}