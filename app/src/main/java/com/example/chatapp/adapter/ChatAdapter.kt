package com.example.chatapp.adapter

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chatapp.databinding.ItemChatBinding
import com.example.chatapp.model.User
import com.example.chatapp.ui.MessageActivity

class ChatAdapter(private val context: Context, private val userList: ArrayList<User>):
    RecyclerView.Adapter<ChatAdapter.ViewHolder>()
{

    private val chatAdapter = ArrayList<User>()
    private lateinit var onItemClickCallback: OnItemClickCallback

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemChatBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    inner class ViewHolder(var binding: ItemChatBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            itemView.setOnClickListener {
                val intent = Intent(it.context, MessageActivity::class.java)
               // intent.putExtra(MessageActivity.EXTRA_USER, user)
                it.context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (name, avatar) = userList[position]
        Glide.with(holder.itemView.context)
            .load(avatar)
            .into(holder.binding.imageView5)
        holder.binding.tvUsername.text = name
        Log.e(TAG, "onBindViewHolder: login")
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(userList[holder.adapterPosition]) }

    }



    interface OnItemClickCallback {
        fun onItemClicked(data: User)
    }
}