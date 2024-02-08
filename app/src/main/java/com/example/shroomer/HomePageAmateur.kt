package com.example.shroomer

import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.shroomer.databinding.ActivityHomePageAmateurBinding
import android.app.Activity
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
import android.graphics.Bitmap
import android.util.Log
import android.widget.Button
class HomePageAmateur : AppCompatActivity() {

    private lateinit var binding: ActivityHomePageAmateurBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomePageAmateurBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d("onCreate() Homepage", savedInstanceState.toString())

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        bottomNavigation.setOnItemSelectedListener{ navigationItem ->
            when(navigationItem.itemId){
                R.id.bottom_home-> {
                    Log.i("HOME NAVIGATION", savedInstanceState.toString())
                    true
                }
                R.id.bottom_upload->{
                    Log.i("UPLOAD NAVIGATION", savedInstanceState.toString())
                    val postUploadPage = Intent(this, PostUploadPage::class.java) //
                    postUploadPage.putExtra("userid","username test")
                    startActivity(postUploadPage)
                    true
                }
                R.id.bottom_profile->{
                    Log.i("MY PROFILE", savedInstanceState.toString())
                    val myProfilePage = Intent(this, MyProfile::class.java) //
                    startActivity(myProfilePage)
                    true
                }
                else->false

             }
        }


    }
//    val loginButton: Button = findViewById(R.id.login_button)

//    signupButton.setOnClickListener {
//        val signUpIntent = Intent(this, SignUpPage::class.java)
//        startActivity(signUpIntent)
//    }
}
