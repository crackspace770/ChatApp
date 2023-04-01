package com.example.chatapp.api

import com.example.chatapp.constant.Constant.Companion.CONTENT_TYPE
import com.example.chatapp.constant.Constant.Companion.SERVER_KEY
import com.example.chatapp.model.PushNotification
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {

    @Headers("Authorization: key=$SERVER_KEY","Content-type:$CONTENT_TYPE")
    @POST("fcm/send")
    suspend fun postNotification(
        @Body notification: PushNotification
    ): Response<ResponseBody>
}