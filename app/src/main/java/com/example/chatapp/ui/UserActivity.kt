package com.example.chatapp.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.chatapp.databinding.ActivityUserBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class UserActivity:AppCompatActivity() {

    private lateinit var binding: ActivityUserBinding
    private lateinit var auth: FirebaseAuth
    private var databaseReference :  DatabaseReference? = null
    private var database: FirebaseDatabase? = null
    private lateinit var uid: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        uid = auth.currentUser?.uid.toString()
        databaseReference = database?.reference!!.child("profile")

        if (uid.isNotEmpty()){

            loadProfile()
        }

    }

    private fun loadProfile(){
        val user = auth.currentUser
        val userreference = databaseReference?.child(user?.uid!!)

        binding.tvEmail.text = user?.email
        binding.tvUsername.text = user?.displayName

        userreference?.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {


                binding.tvEmail.text = snapshot.child("email").value.toString()
                binding.tvUsername.text = snapshot.child("username").value.toString()
                //  binding?.textViewName?.text = snapshot.child("name").value.toString()


            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show()
            }
        })

        binding.btnLogout.setOnClickListener {
            logout()
        }

    }

    private fun logout(){
        auth.signOut()

        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        // showLoading(true)
        startActivity(intent)
        onDestroy()
    }


}