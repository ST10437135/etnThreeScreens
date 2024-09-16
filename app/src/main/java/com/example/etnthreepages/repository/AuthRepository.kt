package com.example.etnthreepages.repository

import com.example.etnthreepages.data.LoginBody
import com.example.etnthreepages.data.SignUpBody
import com.example.etnthreepages.data.ValidateEmailBody
import com.example.etnthreepages.util.APIConsumer
import com.example.etnthreepages.util.RequestStatus
import com.example.etnthreepages.util.SimplifyMessage
import kotlinx.coroutines.flow.flow


class AuthRepository(private val consumer: APIConsumer) {

    fun validateEmailAddress(body: ValidateEmailBody) = flow {
        emit(RequestStatus.Waiting)
        val response = consumer.validateEmail(body)
        if (response.isSuccessful) {
            emit((RequestStatus.Success(response.body()!!)))
        } else (RequestStatus.Error(
            SimplifyMessage.get(
                response.errorBody()!!.byteStream().reader().readText()
            )
        ))

    }

    fun signUpUser(body: SignUpBody) = flow {
        emit(RequestStatus.Waiting)
        val response = consumer.signupUser(body)
        if (response.isSuccessful) {
            emit((RequestStatus.Success(response.body()!!)))
        } else (RequestStatus.Error(
            SimplifyMessage.get(
                response.errorBody()!!.byteStream().reader().readText()
            )
        ))
    }

    fun loginUser(body: LoginBody) = flow {
        emit(RequestStatus.Waiting)
        val response = consumer.loginUser(body)
        if (response.isSuccessful) {
            emit((RequestStatus.Success(response.body()!!)))
        } else (RequestStatus.Error(
            SimplifyMessage.get(
                response.errorBody()!!.byteStream().reader().readText()
            )
        ))
    }

}