package com.example.chatapp.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.chatapp.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignupActivity:AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private var database: FirebaseDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("profile")


        setupView()
        signUp()
    }

    private fun signUp(){

        binding.signupButton.setOnClickListener {

            val userName = binding.usernameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (TextUtils.isEmpty(binding.usernameEditText.text.toString())) {
                binding.usernameEditText.error = "Mohon masukkan nama pengguna! "
                return@setOnClickListener
            } else if (TextUtils.isEmpty(binding.emailEditText.text.toString())) {
                binding.emailEditText.error = "Please enter user email"
                return@setOnClickListener
            } else if (TextUtils.isEmpty(binding.passwordEditText.text.toString())) {
                binding.passwordEditText.error = "Please enter password "
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(

                binding.emailEditText.text.toString(),
                binding.passwordEditText.text.toString(),


                )
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        val currentUser = auth.currentUser
                        val currentUSerDb = databaseReference.child((currentUser?.uid!!))
                        currentUSerDb.child("username").setValue(binding.usernameEditText.text.toString())


                        Toast.makeText(
                            this@SignupActivity,
                            "Berhasil mendaftar, silahkan lanjut login ",
                            Toast.LENGTH_LONG
                        ).show()
                        finish()

                    } else {
                        Toast.makeText(
                            this@SignupActivity,
                            "Gagal mendaftar, silahkan coba lagi! ",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

            registerUser(userName,email,password)

        }

        binding.tvPunyaAkun.setOnClickListener {
            startActivity(Intent(this@SignupActivity, LoginActivity::class.java))
        }
    }

    private fun registerUser(userName:String,email:String,password:String){
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(this){
                if (it.isSuccessful){
                    val user: FirebaseUser? = auth.currentUser
                    val userId:String = user!!.uid

                    databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId)

                    val hashMap:HashMap<String,String> = HashMap()
                    hashMap.put("userId",userId)
                    hashMap.put("userName",userName)
                    hashMap.put("profileImage","")

                    databaseReference.setValue(hashMap).addOnCompleteListener(this){
                        if (it.isSuccessful){
                            //open home activity
                            binding.emailEditText.setText("")
                            binding.usernameEditText.setText("")
                            binding.passwordEditText.setText("")
                            val intent = Intent(this@SignupActivity,
                                ProfileActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                }
            }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }


}