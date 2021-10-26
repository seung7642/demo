package com.pangtrue.firebase_app

import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Query

interface FirebaseTokenService {

    // Token 정보를 서버로 전송한다.
    @POST("token")
    fun uploadToken(@Query("token") token: String) : Call<String>
}