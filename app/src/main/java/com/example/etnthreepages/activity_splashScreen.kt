package com.example.etnthreepages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.etnthreepages.view.SignUpActivity

class activity_splashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val splashBtn = findViewById<Button>(R.id.splashBtn)
// Intent inspired by Kincade as he showed how to access a different screen by pressing a button
        splashBtn.setOnClickListener{
            val intent = Intent(this , SignUpActivity::class.java)
            startActivity(intent)

        }
    }
}