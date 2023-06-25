package com.example.smartfootapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartfootapp.R
import com.example.smartfootapp.db.entity.User

class UserAdapter(
    private val onDeleteClicked: (User) -> Unit,
    private val onEditClicked: (User) -> Unit
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private var users: List<User> = emptyList()

    fun submitData(newUserList: List<User>) {
        users = newUserList.map { it.copy() }
        notifyDataSetChanged()
    }

    inner class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView = view.findViewById<TextView>(R.id.tv_user_name)
        val editImageView = view.findViewById<ImageView>(R.id.iv_edit)
        val deleteImageView = view.findViewById<ImageView>(R.id.iv_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)

        return UserViewHolder(itemView)
    }

    override fun getItemCount(): Int = users.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val item = users[position]

        holder.nameTextView.text = item.name
        holder.deleteImageView.setOnClickListener {
            onDeleteClicked.invoke(item)
        }
        holder.editImageView.setOnClickListener {
            onEditClicked.invoke(item)
        }
    }
}