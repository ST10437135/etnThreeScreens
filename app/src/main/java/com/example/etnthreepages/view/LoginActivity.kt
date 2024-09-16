package com.example.etnthreepages.view

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.etnthreepages.R
import com.example.etnthreepages.activity_homePage
import com.example.etnthreepages.data.LoginBody
import com.example.etnthreepages.data.ValidateEmailBody
import com.example.etnthreepages.databinding.ActivityLoginBinding
import com.example.etnthreepages.repository.AuthRepository
import com.example.etnthreepages.util.APIService
import com.example.etnthreepages.util.VibrationView
import com.example.etnthreepages.view_model.LoginActivityViewModel
import com.example.etnthreepages.view_model.LoginActivityViewModelFactory
import java.lang.StringBuilder

class LoginActivity : AppCompatActivity(), View.OnClickListener, View.OnFocusChangeListener, View.OnKeyListener {

    private lateinit var mBinding: ActivityLoginBinding
    private lateinit var mViewModel: LoginActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mBinding = ActivityLoginBinding.inflate(LayoutInflater.from(this))
        setContentView(mBinding.root)

        mBinding.loginBtn.setOnKeyListener(this)
        mBinding.emailTxtInp.onFocusChangeListener = this
        mBinding.passwordTxtInp.onFocusChangeListener = this
        mBinding.passwordTxtInp.setOnKeyListener(this)
        setupObservers()

        mViewModel = ViewModelProvider(this, LoginActivityViewModelFactory(AuthRepository(APIService.getService()), application)).get(LoginActivityViewModel::class.java)

    }
    // Vailidates the email input field
    private fun validateEmail(shouldUpdateView: Boolean = true, shouldVibrateView: Boolean = true): Boolean {
        var errorMessage: String? = null
        val value: String = mBinding.emailTxtInp.text.toString()
        if (value.isEmpty()) {
            errorMessage = "Email is required"
        } else if (Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
            errorMessage = "Email Address is invalid"
        }

        // Display error message if validation fails
        if (errorMessage != null && shouldUpdateView) {
            mBinding.emailTxtInpLyt.apply {
                isErrorEnabled = true
                error = errorMessage
                if (shouldVibrateView) VibrationView.vibrate(this@LoginActivity, this)
            }
        }

        // Return true if there is no error
        return errorMessage == null
    }

    private fun setupObservers(){
        mViewModel.getIsLoading().observe(this){
            mBinding.progressBar.isVisible = it
        }

        mViewModel.getErrorMessage().observe(this){
            //fullName, email, password
            val forErrorKeys = arrayOf("fullName", "email", "password")
            val message = StringBuilder()
            it.map {entry ->
                if (forErrorKeys.contains(entry.key)){
                    when(entry.key){
                        "email" -> {
                            mBinding.emailTxtInpLyt.apply {
                                isErrorEnabled = true
                                error = entry.value
                            }
                        }
                        "password" -> {
                            mBinding.passwordTxtInpLyt.apply {
                                isErrorEnabled = true
                                error = entry.value
                            }
                        }
                    }
                }else{
                    message.append(entry.value).append("\n")
                }

                if (message.isNotEmpty()){
                    AlertDialog.Builder(this)
                        .setIcon(R.drawable.info_24)
                        .setTitle("Information")
                        .setMessage(message)
                        .setPositiveButton("OK"){dialog, _ -> dialog!!.dismiss()}
                        .show()
                }
            }
        }
        mViewModel.getUser().observe(this){
            if (it != null){
                startActivity(Intent(this, activity_homePage::class.java))
            }
        }
    }


    // Validates the password input field
    private fun validatePassword(shouldUpdateView: Boolean = true, shouldVibrateView: Boolean = true): Boolean {
        var errorMessage: String? = null
        val value: String = mBinding.passwordTxtInp.text.toString()
        if (value.isEmpty()) {
            errorMessage = "Password is required"
        } else if (value.length < 6) {
            errorMessage = "Password must 6 characters long"
        }

        // Display error message if validation fails
        if (errorMessage != null && shouldUpdateView) {
            mBinding.passwordTxtInpLyt.apply {
                isErrorEnabled = true
                error = errorMessage
                if (shouldVibrateView) VibrationView.vibrate(this@LoginActivity, this)
            }
        }

        // Return true if there is no error
        return errorMessage == null
    }

    private fun validate(): Boolean {
        var isValid = true

        if (!validateEmail(shouldVibrateView = false)) isValid = false
        if (!validatePassword(shouldVibrateView = false)) isValid = false
        if(!isValid)VibrationView.vibrate(this, mBinding.cardView)

        return isValid
    }

    override fun onClick(view: View?) {
        if (view != null){
            when(view.id){
                R.id.loginBtn -> {
                    submitForm()
                }
            }
        }
    }

    override fun onFocusChange(view: View?, hasFocus: Boolean) {
        if (view != null) {
            when (view.id) {

                R.id.emailTxtInp -> {
                    if (hasFocus) {
                        if (mBinding.emailTxtInpLyt.isErrorEnabled) {
                            mBinding.emailTxtInpLyt.isErrorEnabled = false
                        }
                    } else {
                        validateEmail()
                    }
                }

                R.id.passwordTxtInp -> {
                    if (hasFocus) {
                        if (mBinding.passwordTxtInpLyt.isErrorEnabled) {
                            mBinding.passwordTxtInpLyt.isErrorEnabled = false
                        }
                    } else {
                        validatePassword()
                    }
                }
            }
        }
    }

    private fun submitForm(){
        if(validate()){
            //verify user credentials
            mViewModel.loginUser(LoginBody(mBinding.emailTxtInp.text!!.toString(), mBinding.passwordTxtInp!!.toString()))
        }
    }
    override fun onKey(view: View?, keyCode: Int, keyEvent: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_ENTER && keyEvent!!.action == KeyEvent.ACTION_UP)
            submitForm()
        return false
    }
}