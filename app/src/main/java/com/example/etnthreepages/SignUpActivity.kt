package com.example.etnthreepages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import com.example.etnthreepages.databinding.ActivitySignupBinding

class SignUpActivity : AppCompatActivity(), View.OnClickListener, View.OnFocusChangeListener, View.OnKeyListener {

    private lateinit var mBinding: ActivitySignupBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySignupBinding.inflate(LayoutInflater.from(this))
        setContentView(mBinding.root)
        mBinding.nameTxtInp.onFocusChangeListener = this
        mBinding.emailTxtInp.onFocusChangeListener = this
        mBinding.passwordTxtInp.onFocusChangeListener = this
        mBinding.confirmpTxtInp.onFocusChangeListener = this

        val signUpBtn = findViewById<Button>(R.id.signUpBtn)

        signUpBtn.setOnClickListener{
            val intent = Intent(this , activity_homePage::class.java)
            startActivity(intent)

        }
    }

    private fun validateName(): Boolean{
        var errorMessage: String? = null
        val value: String = mBinding.nameTxtInp.text.toString()
        if (value.isEmpty()){
            errorMessage = "Name is required"
        }

        if(errorMessage != null){
            mBinding.nameTxtInpLyt.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        }

        return errorMessage == null
    }

    private fun validateEmail(): Boolean{
        var errorMessage: String? = null
        val value: String = mBinding.emailTxtInp.text.toString()
        if (value.isEmpty()){
            errorMessage = "Email is required"
        }else if(Patterns.EMAIL_ADDRESS.matcher(value).matches()){
            errorMessage = "Email Address is invalid"
        }

        if(errorMessage != null){
            mBinding.emailTxtInpLyt.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        }

        return errorMessage == null
    }

    private fun validatePassword(): Boolean{
        var errorMessage: String? = null
        val value: String = mBinding.passwordTxtInp.text.toString()
        if (value.isEmpty()){
            errorMessage = "Password is required"
        }else if(value.length < 6){
            errorMessage = "Password must 6 characters long"
        }

        if(errorMessage != null){
            mBinding.passwordTxtInpLyt.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        }

        return errorMessage == null
    }

    private fun validateConfirmPassword(): Boolean{
        var errorMessage: String? = null
        val value = mBinding.confirmpTxtInp.text.toString()
        if (value.isEmpty()){
            errorMessage = "Confirm Password is required"
        }else if(value.length < 6) {
            errorMessage = "Confirm Password must be 6 characters long"
        }

        if(errorMessage != null){
            mBinding.confirmpTxtInpLyt.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        }

        return errorMessage == null
    }

    private fun validatePasswordAndConfirmPassword(): Boolean{
        var errorMessage: String? = null
        val password = mBinding.passwordTxtInp.text.toString()
        val confirmPassword = mBinding.confirmpTxtInp.text.toString()
        if(password != confirmPassword){
            errorMessage = "Confirm Password doesn't match with password"
        }

        if(errorMessage != null){
            mBinding.confirmpTxtInpLyt.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        }

        return errorMessage == null
    }

    override fun onClick(view: View?) {
        TODO("Not yet implemented")
    }

    override fun onFocusChange(view: View?, hasFocus: Boolean) {
        if(view != null){
            when(view.id){
                R.id.nameTxtInp -> {
                    if(hasFocus){
                        if(mBinding.nameTxtInpLyt.isErrorEnabled){
                            mBinding.nameTxtInpLyt.isErrorEnabled = false
                        }
                    }else{
                        validateName()
                    }
                }
                R.id.emailTxtInp -> {
                    if(hasFocus){
                        if(mBinding.emailTxtInpLyt.isErrorEnabled){
                            mBinding.emailTxtInpLyt.isErrorEnabled = false
                        }
                    }else{
                        validateEmail()
                    }
                }
                R.id.passwordTxtInp -> {
                    if(hasFocus){
                        if(mBinding.passwordTxtInpLyt.isErrorEnabled){
                            mBinding.passwordTxtInpLyt.isErrorEnabled = false
                        }
                    }else{
                        if(validatePassword() && mBinding.confirmpTxtInp.text!!.isNotEmpty() && validateConfirmPassword() && validatePasswordAndConfirmPassword()){
                            if(mBinding.confirmpTxtInpLyt.isErrorEnabled){
                                mBinding.confirmpTxtInpLyt.isErrorEnabled = false

                            }
                            mBinding.confirmpTxtInpLyt.setStartIconDrawable(R.drawable.check_circle_24)
                        }
                    }
                }
                R.id.confirmpTxtInp -> {
                    if(hasFocus){
                        if(mBinding.confirmpTxtInpLyt.isErrorEnabled){
                            mBinding.confirmpTxtInpLyt.isErrorEnabled = false
                        }
                    }else{
                        if(validateConfirmPassword() && validatePassword() && validatePasswordAndConfirmPassword()){
                            if(mBinding.passwordTxtInpLyt.isErrorEnabled){
                                mBinding.passwordTxtInpLyt.isErrorEnabled = false
                            }
                            mBinding.confirmpTxtInpLyt.setStartIconDrawable(R.drawable.check_circle_24)
                        }
                    }
                }
            }
        }
    }

    override fun onKey(view: View?, event: Int, keyEvent: KeyEvent?): Boolean {
        return  false
    }
}