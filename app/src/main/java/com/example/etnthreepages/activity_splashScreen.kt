package com.example.etnthreepages

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.etnthreepages.view.LoginActivity
import com.example.etnthreepages.view.SignUpActivity

class activity_splashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val signUpViewBtn = findViewById<Button>(R.id.splashSignUpBtn)
        val loginViewBtn = findViewById<Button>(R.id.splashLoginBtn)
// Intent inspired by Kincade as he showed how to access a different screen by pressing a button
        signUpViewBtn.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)

        }

        loginViewBtn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}