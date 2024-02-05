package com.example.shroomer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class HomePageAmateur : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page_amateur)

        val newpostButton: Button = findViewById(R.id.bottom_navigation)
        newpostButton.setOnClickListener {
            val newPostIntent = Intent(this, PostUploadPage::class.java)
            startActivity(newPostIntent)
        }
    }
//    val loginButton: Button = findViewById(R.id.login_button)

//    signupButton.setOnClickListener {
//        val signUpIntent = Intent(this, SignUpPage::class.java)
//        startActivity(signUpIntent)
//    }
}