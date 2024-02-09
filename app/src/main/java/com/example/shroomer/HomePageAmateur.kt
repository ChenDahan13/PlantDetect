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
import android.widget.Toast
import androidx.fragment.app.Fragment

class HomePageAmateur : AppCompatActivity() {

    private lateinit var binding: ActivityHomePageAmateurBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomePageAmateurBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d("onCreate() Homepage", savedInstanceState.toString())

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        //get Parcelable User
        val myUser: User? = intent.getParcelableExtra("my_user_parcelable")
        Toast.makeText(this, "Hello "+myUser?.getUsername(), Toast.LENGTH_SHORT).show()

        val bundle =Bundle()
        bundle.putParcelable("my_user_parcelable",myUser)

        val fragmentHomePage = FragmentHomePage()
        val fragmentNewPost = FragmentNewPost()
        val fragmentMyProfile = FragmentMyProfile()

        fragmentHomePage.arguments = bundle
        fragmentNewPost.arguments = bundle
        fragmentMyProfile.arguments = bundle


        replaceFragment(fragmentHomePage)

        bottomNavigation.setOnItemSelectedListener{ navigationItem ->
            when(navigationItem.itemId){
                R.id.bottom_home-> {
                    replaceFragment(fragmentHomePage)
                    //Log.i("HOME NAVIGATION", savedInstanceState.toString())
                    true
                }
                R.id.bottom_upload->{
                    Log.i("UPLOAD NAVIGATION", savedInstanceState.toString())
                    replaceFragment(fragmentNewPost)
                    /*val postUploadPage = Intent(this, PostUploadPage::class.java) //
                    postUploadPage.putExtra("userid","username test")
                    startActivity(postUploadPage)*/
                    true
                }
                R.id.bottom_profile->{
                    Log.i("MY PROFILE", savedInstanceState.toString())/*
                    val myProfilePage = Intent(this, MyProfile::class.java) //
                    startActivity(myProfilePage)*/
                    replaceFragment(fragmentMyProfile)
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
