package com.example.etnthreepages.view_model

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.etnthreepages.data.SignUpBody
import com.example.etnthreepages.data.User
import com.example.etnthreepages.data.ValidateEmailBody
import com.example.etnthreepages.repository.AuthRepository
import com.example.etnthreepages.util.RequestStatus
import com.example.etnthreepages.util.TokenAuth
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

// SignUpActivityModel is a ViewModel class that holds and manages UI-related data for the SignUpActvity
// It is designed to survive configuration changes like screen rotations
class SignUpActivityViewModel(val authRepository: AuthRepository, val application: Application) :
    ViewModel() {

    // MutableLiveData is used to store and manage the loading state
    // Initially, the loading state is set to false (not loading)
    private var isLoading: MutableLiveData<Boolean> =
        MutableLiveData<Boolean>().apply { value = false }

    //MutableLiveData to store error massages as a HashMap
    // This allows multiple error messages to be tracked and displayed in the UI
    private var errorMessage: MutableLiveData<HashMap<String, String>> = MutableLiveData()

    // MutableLiveData to track the uniqueness of user data (e.g., if the email is unique)
    // Initially set to false (not unique)
    private var isUnique: MutableLiveData<Boolean> =
        MutableLiveData<Boolean>().apply { value = false }

    // MutableLive Data to store the User object that represents the signed-up user
    private var user: MutableLiveData<User> = MutableLiveData()

    // Function to expose loading state as LiveData
    // LiveData is immutable from the observer's perspective, ensuring that only this ViewModel can update the data
    fun getIsLoading(): LiveData<Boolean> = isLoading

    // Function to expose error messages as LiveData
    // This allows the UI to observe changes in the error messages and react accordingly
    fun getErrorMessage(): LiveData<HashMap<String, String>> = errorMessage

    // Function to expose the uniqueness state as LiveData
    // The UI can observe this to update the state (e.g., to show a checkmark if the email is unique)
    fun getIsUnique(): LiveData<Boolean> = isUnique

    // Function to expose the User object as LiveData
    // This allows the UI to obeserve data, typically after a successful sign-up
    fun getUser(): LiveData<User> = user

    fun validateEmailAddress(body: ValidateEmailBody) {
        viewModelScope.launch {
            authRepository.validateEmailAddress(body).collect {
                when(it){
                    is RequestStatus.Waiting -> {
                        isLoading.value = true
                    }
                    is RequestStatus.Success -> {
                        isLoading.value = false
                        isUnique.value = it.data.isUnique
                    }
                    is RequestStatus.Error -> {
                        isLoading.value = false
                        errorMessage.value = it.message
                    }
                }
            }
        }

    }

    fun signUpUser(body: SignUpBody){
        viewModelScope.launch {
            authRepository.signUpUser(body).collect {
                when(it){
                    is RequestStatus.Waiting -> {
                        isLoading.value = true
                    }
                    is RequestStatus.Success -> {
                        isLoading.value = false
                        user.value = it.data.user
                        // save token using shared preferences
                        TokenAuth.getInstance(application.baseContext).token = it.data.token
                    }
                    is RequestStatus.Error -> {
                        isLoading.value = false
                        errorMessage.value = it.message
                    }
                }
            }
        }
    }
}