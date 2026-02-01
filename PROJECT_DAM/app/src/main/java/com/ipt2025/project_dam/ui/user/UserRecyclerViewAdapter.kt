package com.ipt2025.project_dam.ui.user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ipt2025.project_dam.data.api.UserData
import com.ipt2025.project_dam.databinding.FragmentUserListItemBinding

class UserRecyclerViewAdapter(
    private val users: MutableList<UserData>,
    private val onUserClick: (UserData) -> Unit
) : RecyclerView.Adapter<UserRecyclerViewAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = FragmentUserListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]
        holder.bind(user, onUserClick)
    }

    override fun getItemCount(): Int = users.size

    fun addUsers(newUsers: List<UserData>) {
        val startPosition = users.size
        users.addAll(newUsers)
        notifyItemRangeInserted(startPosition, newUsers.size)
    }

    class UserViewHolder(private val binding: FragmentUserListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: UserData, onUserClick: (UserData) -> Unit) {
            binding.userName.text = user.name
            binding.userEmail.text = user.email ?: "No email"
            binding.userRole.text = user.role.capitalize()

            // Set chip color based on role
            when (user.role.lowercase()) {
                "admin" -> binding.userRole.setChipBackgroundColorResource(android.R.color.holo_red_light)
                "tech" -> binding.userRole.setChipBackgroundColorResource(android.R.color.holo_blue_light)
                else -> binding.userRole.setChipBackgroundColorResource(android.R.color.holo_green_light)
            }

            binding.root.setOnClickListener {
                // FIXED: Only call onUserClick if _id is not null
                user._id?.let {
                    onUserClick(user)
                }
            }
        }
    }
}