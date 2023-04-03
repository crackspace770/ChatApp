package com.example.chatapp.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatapp.adapter.ChatAdapter
import com.example.chatapp.databinding.ActivityChatBinding
import com.example.chatapp.firebase.FirebaseService
import com.example.chatapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.messaging.FirebaseMessaging
import java.util.ArrayList
import com.google.firebase.iid.FirebaseInstanceIdReceiver

class ChatActivity:AppCompatActivity() {

    var userList = ArrayList<User>()
    private lateinit var binding: ActivityChatBinding
    private lateinit var adapter: ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        FirebaseService.sharedPref = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
      //  FirebaseInstanceIdReceiver.getInstance().instanceId.addOnSuccessListener {
    //        FirebaseService.token = it.token
    //    }

        //binding recylerview
        val layoutManager = LinearLayoutManager(this)
        binding.rvChat.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvChat.addItemDecoration(itemDecoration)

      //  messageRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)


        getUsersList()
    }

    private fun getUsersList() {
        val firebase: FirebaseUser = FirebaseAuth.getInstance().currentUser!!

        val userid = firebase.uid
        FirebaseMessaging.getInstance().subscribeToTopic("/topics/$userid")


        val databaseReference: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("Users")


        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
        //        val currentUser = snapshot.getValue(User::class.java)

             //   if (currentUser!!.imgProfile== ""){
             //       imgProfile.setImageResource(com.google.firebase.database.R.drawable.profile_image)
            //    }else{
           //         Glide.with(this@ChatActivity).load(currentUser.profileImage).into(imgProfile)
           //     }

                /*
                for (dataSnapShot: DataSnapshot in snapshot.children) {
                    val user = dataSnapShot.getValue(User::class.java)

                    if (user!!.id != firebase.uid) {

                        userList.add(user)
                    }
                }
            */
                val userAdapter = ChatAdapter(this@ChatActivity, userList)

                binding.rvChat.adapter = userAdapter
            }

        })
    }
}

