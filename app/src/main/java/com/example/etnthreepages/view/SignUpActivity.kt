package com.example.etnthreepages.view

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.etnthreepages.R
import com.example.etnthreepages.activity_homePage
import com.example.etnthreepages.data.SignUpBody
import com.example.etnthreepages.data.ValidateEmailBody
import com.example.etnthreepages.databinding.ActivitySignupBinding
import com.example.etnthreepages.repository.AuthRepository
import com.example.etnthreepages.util.APIService
import com.example.etnthreepages.util.VibrationView
import com.example.etnthreepages.view_model.SignUpActivityViewModel
import com.example.etnthreepages.view_model.SignUpActivityViewModelFactory
import java.lang.StringBuilder

class SignUpActivity : AppCompatActivity(), View.OnClickListener, View.OnFocusChangeListener,
    View.OnKeyListener, TextWatcher {

    // View binding object to access UI elements in the layout
    private lateinit var mBinding: ActivitySignupBinding

    private lateinit var mViewModel: SignUpActivityViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout and bind it to mBinding
        mBinding = ActivitySignupBinding.inflate(LayoutInflater.from(this))
        setContentView(mBinding.root)

        // Set focus change listeners for input fields
        mBinding.nameTxtInp.onFocusChangeListener = this
        mBinding.emailTxtInp.onFocusChangeListener = this
        mBinding.passwordTxtInp.onFocusChangeListener = this
        mBinding.confirmpTxtInp.onFocusChangeListener = this
        mBinding.confirmpTxtInp.setOnKeyListener(this)
        mBinding.confirmpTxtInp.addTextChangedListener(this)
        mViewModel = ViewModelProvider(this, SignUpActivityViewModelFactory(AuthRepository(APIService.getService()), application)).get(SignUpActivityViewModel::class.java)


        val signUpBtn = findViewById<Button>(R.id.signUpBtn)

        // Start the HomPage Activity upon successful sign-up
        signUpBtn.setOnClickListener {
            val intent = Intent(this, activity_homePage::class.java)
            startActivity(intent)

        }

        setupObservers()
    }

    private fun setupObservers(){
        mViewModel.getIsLoading().observe(this){
            mBinding.progressBar.isVisible = it
        }

        mViewModel.getIsUnique().observe(this){
            if (validateEmail(shouldUpdateView = false)){
            if (it){
                mBinding.emailTxtInpLyt.apply {
                    if (isErrorEnabled) isErrorEnabled = false
                    setStartIconDrawable(R.drawable.check_circle_24)
                    setStartIconTintList(ColorStateList.valueOf(Color.GREEN))
                }
            }else {
                mBinding.emailTxtInpLyt.apply {
                    if (startIconDrawable != null) startIconDrawable = null
                    isErrorEnabled = true
                    error = "Email is already taken"
                }
            }
            }
        }
        mViewModel.getErrorMessage().observe(this){
            //fullName, email, password
            val forErrorKeys = arrayOf("fullName", "email", "password")
            val message = StringBuilder()
            it.map {entry ->
                if (forErrorKeys.contains(entry.key)){
                    when(entry.key){
                        "fullName" -> {
                            mBinding.nameTxtInpLyt.apply {
                                isErrorEnabled = true
                                error = entry.value
                            }
                        }
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

    //Validate the name input field
    private fun validateName(shouldVibrateView: Boolean = true): Boolean {
        var errorMessage: String? = null
        val value: String = mBinding.nameTxtInp.text.toString()
        if (value.isEmpty()) {
            errorMessage = "Name is required"
        }

        // Display error if validation fails
        if (errorMessage != null) {
            mBinding.nameTxtInpLyt.apply {
                isErrorEnabled = true
                error = errorMessage
                if (shouldVibrateView)VibrationView.vibrate(this@SignUpActivity, this)
            }
        }

        // Return true if there is no error
        return errorMessage == null
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
                if (shouldVibrateView)VibrationView.vibrate(this@SignUpActivity, this)
            }
        }

        // Return true if there is no error
        return errorMessage == null
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
                if (shouldVibrateView)VibrationView.vibrate(this@SignUpActivity, this)
            }
        }

        // Return true if there is no error
        return errorMessage == null
    }

    // Validates the confirm password input field
    private fun validateConfirmPassword(shouldUpdateView: Boolean = true, shouldVibrateView: Boolean = true): Boolean {
        var errorMessage: String? = null
        val value = mBinding.confirmpTxtInp.text.toString()
        if (value.isEmpty()) {
            errorMessage = "Confirm Password is required"
        } else if (value.length < 6) {
            errorMessage = "Confirm Password must be 6 characters long"
        }

        // Display error message if validation fails
        if (errorMessage != null && shouldUpdateView) {
            mBinding.confirmpTxtInpLyt.apply {
                isErrorEnabled = true
                error = errorMessage
                if (shouldVibrateView)VibrationView.vibrate(this@SignUpActivity, this)
            }
        }

        // Return true if there is no error
        return errorMessage == null
    }

    // Validates if password and confirm password fields match
    private fun validatePasswordAndConfirmPassword(shouldUpdateView: Boolean = true, shouldVibrateView: Boolean = true): Boolean {
        var errorMessage: String? = null
        val password = mBinding.passwordTxtInp.text.toString()
        val confirmPassword = mBinding.confirmpTxtInp.text.toString()
        if (password != confirmPassword) {
            errorMessage = "Confirm Password doesn't match with password"
        }

        // Display error if validation fails
        if (errorMessage != null && shouldUpdateView) {
            mBinding.confirmpTxtInpLyt.apply {
                isErrorEnabled = true
                error = errorMessage
                if (shouldVibrateView)VibrationView.vibrate(this@SignUpActivity, this)
            }
        }

        // Return true if there is no error
        return errorMessage == null
    }

    // Handles click events for views in the activity
    override fun onClick(view: View?) {
        if (view != null && view.id == R.id.signUpBtn)
            userSubmit()
    }

    // Handles focuc change events for views in the activity
    override fun onFocusChange(view: View?, hasFocus: Boolean) {
        if (view != null) {
            when (view.id) {
                R.id.nameTxtInp -> {
                    if (hasFocus) {
                        // Clear error message when input gains focus
                        if (mBinding.nameTxtInpLyt.isErrorEnabled) {
                            mBinding.nameTxtInpLyt.isErrorEnabled = false
                        }
                    } else {
                        // Validate name when input loses focus
                        validateName()
                    }
                }

                R.id.emailTxtInp -> {
                    if (hasFocus) {
                        if (mBinding.emailTxtInpLyt.isErrorEnabled) {
                            mBinding.emailTxtInpLyt.isErrorEnabled = false
                        }
                    } else {
                        if (validateEmail()) {
                            mViewModel.validateEmailAddress((ValidateEmailBody(mBinding.emailTxtInp.text!!.toString())))
                        }
                    }
                }

                R.id.passwordTxtInp -> {
                    if (hasFocus) {
                        if (mBinding.passwordTxtInpLyt.isErrorEnabled) {
                            mBinding.passwordTxtInpLyt.isErrorEnabled = false
                        }
                    } else {
                        if (validatePassword() && mBinding.confirmpTxtInp.text!!.isNotEmpty() && validateConfirmPassword() && validatePasswordAndConfirmPassword()) {
                            if (mBinding.confirmpTxtInpLyt.isErrorEnabled) {
                                mBinding.confirmpTxtInpLyt.isErrorEnabled = false

                            }
                            mBinding.confirmpTxtInpLyt.setStartIconDrawable(R.drawable.check_circle_24)
                        }
                    }
                }

                R.id.confirmpTxtInp -> {
                    if (hasFocus) {
                        if (mBinding.confirmpTxtInpLyt.isErrorEnabled) {
                            mBinding.confirmpTxtInpLyt.isErrorEnabled = false
                        }
                    } else {
                        if (validateConfirmPassword() && validatePassword() && validatePasswordAndConfirmPassword()) {
                            if (mBinding.passwordTxtInpLyt.isErrorEnabled) {
                                mBinding.passwordTxtInpLyt.isErrorEnabled = false
                            }
                            mBinding.confirmpTxtInpLyt.setStartIconDrawable(R.drawable.check_circle_24)
                        }
                    }
                }
            }
        }
    }

    // Handles key events for views in the activity
    override fun onKey(view: View?, keyCode: Int, keyEvent: KeyEvent?): Boolean {
        // Curently, this method does nothing and always return false
        if (KeyEvent.KEYCODE_ENTER == keyCode && keyEvent!!.action == KeyEvent.ACTION_UP){
            userSubmit()

        }

        return false
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if(validatePassword(shouldUpdateView = false) && validateConfirmPassword(shouldUpdateView = false) && validatePasswordAndConfirmPassword(shouldUpdateView = false)){
            mBinding.confirmpTxtInpLyt.apply {
                if(isErrorEnabled) isErrorEnabled = false
                setStartIconDrawable(R.drawable.check_circle_24)
                setStartIconTintList(ColorStateList.valueOf(Color.GREEN))
            }
        }else {
            if(mBinding.confirmpTxtInpLyt.startIconDrawable !=null)
                mBinding.confirmpTxtInpLyt.startIconDrawable = null
        }
    }

    override fun afterTextChanged(s: Editable?) {

    }

    private fun userSubmit(){
        if (validate()){
            //make api request
            mViewModel.signUpUser((SignUpBody(mBinding.nameTxtInp.text!!.toString(), mBinding.emailTxtInp.text!!.toString(), mBinding.passwordTxtInp.text!!.toString() )))
        }
    }

    private fun validate(): Boolean {
        var isValid = true

        if (!validateEmail(shouldVibrateView = false)) isValid = false
        if (!validateName(shouldVibrateView = false)) isValid = false
        if (!validatePassword(shouldVibrateView = false)) isValid = false
        if (!validateConfirmPassword(shouldVibrateView = false)) isValid = false
        if (isValid && !validatePasswordAndConfirmPassword(shouldVibrateView = false)) isValid = false
        if(!isValid)VibrationView.vibrate(this, mBinding.cardView)

        return isValid
    }
}