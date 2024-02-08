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
import androidx.fragment.app.Fragment

class HomePageAmateur : AppCompatActivity() {

    private lateinit var binding: ActivityHomePageAmateurBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomePageAmateurBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d("onCreate() Homepage", savedInstanceState.toString())

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        replaceFragment(FragmentHomePage())

        bottomNavigation.setOnItemSelectedListener{ navigationItem ->
            when(navigationItem.itemId){
                R.id.bottom_home-> {
                    replaceFragment(FragmentHomePage())
                    //Log.i("HOME NAVIGATION", savedInstanceState.toString())
                    true
                }
                R.id.bottom_upload->{
                    Log.i("UPLOAD NAVIGATION", savedInstanceState.toString())
                    replaceFragment(FragmentNewPost())
                    /*val postUploadPage = Intent(this, PostUploadPage::class.java) //
                    postUploadPage.putExtra("userid","username test")
                    startActivity(postUploadPage)*/
                    true
                }
                R.id.bottom_profile->{
                    Log.i("MY PROFILE", savedInstanceState.toString())/*
                    val myProfilePage = Intent(this, MyProfile::class.java) //
                    startActivity(myProfilePage)*/
                    replaceFragment(FragmentMyProfile())
                    true
                }
                else->false

             }
        }


    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val currentFragment = fragmentManager.findFragmentById(R.id.fragmentContainer)
        if (currentFragment==null || currentFragment.javaClass!=fragment.javaClass){
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragmentContainer,fragment)
            fragmentTransaction.commit()
        }
    }
//    val loginButton: Button = findViewById(R.id.login_button)

//    signupButton.setOnClickListener {
//        val signUpIntent = Intent(this, SignUpPage::class.java)
//        startActivity(signUpIntent)
//    }
}
