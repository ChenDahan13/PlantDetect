package com.example.shroomer.Homepage

import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.shroomer.databinding.ActivityHomePageAmateurBinding
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.shroomer.Entities.User
import com.example.shroomer.R

class HomePageAmateur : AppCompatActivity() {

    private lateinit var binding: ActivityHomePageAmateurBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomePageAmateurBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d("onCreate() Homepage", savedInstanceState.toString())

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        // get Parcelable User
        val myUser: User? = intent.getParcelableExtra("my_user_parcelable")


        val bundle =Bundle()
        bundle.putParcelable("my_user_parcelable",myUser)

        val fragmentHomePage = FragmentHomePage()
        val fragmentMyProfile = FragmentMyProfile()
        val fragmentNewPostTry = FragmentNewPostTry()
        val fragmentMapPage = mapPage()

        fragmentMapPage.arguments = bundle
        fragmentNewPostTry.arguments = bundle
        fragmentHomePage.arguments = bundle
        fragmentMyProfile.arguments = bundle


        replaceFragment(fragmentHomePage)

        bottomNavigation.setOnItemSelectedListener{ navigationItem ->
            when(navigationItem.itemId){
                R.id.bottom_home -> {
                    replaceFragment(fragmentHomePage)
                    true
                }
                R.id.bottom_upload -> {
                    Log.i("UPLOAD NAVIGATION", savedInstanceState.toString())
                    replaceFragment(fragmentNewPostTry)
                    true
                }
                R.id.bottom_profile ->{
                    Log.i("MY PROFILE", savedInstanceState.toString())
                    replaceFragment(fragmentMyProfile)
                    true
                }
                R.id.bottom_map ->{
                    Log.i("MAP NAVIGATION", savedInstanceState.toString())
                    replaceFragment(fragmentMapPage)
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
            fragmentTransaction.add(FragmentHomePage(),"home_page")
            fragmentTransaction.replace(R.id.fragmentContainer,fragment)
            fragmentTransaction.commit()
        }
    }
}
