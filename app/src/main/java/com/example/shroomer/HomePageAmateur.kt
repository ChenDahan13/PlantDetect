package com.example.shroomer

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
class HomePageAmateur : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page_amateur)

        val newpostButton = findViewById<Button>(R.id.bottom_navigation)
        newpostButton.setOnClickListener {
            val newPostIntent = Intent(this, PostUploadPage::class.java)
            startActivity(newPostIntent)
        }
    }
}