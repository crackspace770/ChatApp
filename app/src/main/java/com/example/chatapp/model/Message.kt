package com.example.chatapp.model

data class Message (
    var senderId:String = "",
    var receiverId:String = "",
    var message:String = "",
    val timestamp: Long? = null
        )