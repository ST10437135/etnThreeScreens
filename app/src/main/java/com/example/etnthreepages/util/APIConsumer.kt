package com.example.etnthreepages.util

import com.example.etnthreepages.data.LoginBody
import com.example.etnthreepages.data.SignUpBody
import com.example.etnthreepages.data.ResponseAuth
import com.example.etnthreepages.data.UniqueEmailValRes
import com.example.etnthreepages.data.ValidateEmailBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface APIConsumer {

    @POST("user/validate-unique-email")
    suspend fun validateEmail(@Body body: ValidateEmailBody): Response<UniqueEmailValRes>

    @POST("user/signup")
    suspend fun signupUser(@Body body: SignUpBody): Response<ResponseAuth>

    @POST("user/login")
    suspend fun loginUser(@Body body: LoginBody): Response<ResponseAuth>
}