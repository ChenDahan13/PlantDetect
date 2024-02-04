package com.example.shroomer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.shroomer.databinding.ActivityMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    // Set the database reference
    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReferenceAmateur: DatabaseReference
    private lateinit var databaseReferenceExpert: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Create the database reference
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReferenceAmateur = firebaseDatabase.reference.child("Amateur")
        databaseReferenceExpert = firebaseDatabase.reference.child("Expert")

        // References to buttons
        val loginButton: Button = findViewById(R.id.login_button)
        val signupButton: Button = findViewById(R.id.signup_button)

        // Clicking on the login button
        loginButton.setOnClickListener {
            val username = binding.loginusername.text.toString()
            val password = binding.loginpassword.text.toString()
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val isExist = isUserExist(username, password)
            if (isExist == 1) {
                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                val homePageIntent = Intent(this, HomePageAmateur::class.java)
                startActivity(homePageIntent)
            } else {
                Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show()
            }
        }

        // Clicking on the sign up button
        signupButton.setOnClickListener {
            val signUpIntent = Intent(this, SignUpPage::class.java)
            startActivity(signUpIntent)
        }
    }

    private fun isUserExist(username:String, password: String): Int {
        var isExist: Int = 0
        databaseReferenceAmateur.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) { // Check if the user exists in the amateur database
                if (snapshot.exists()) {
                    for (amateurUser in snapshot.children) {
                        val userPassword = amateurUser.child("password").getValue(String::class.java)
                        if (userPassword == password) { // If the user password is right, set the isExist variable to 1
                            isExist = 1
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
        if (isExist == 1) { // The user exists in the amateur database
            return isExist
        }
        databaseReferenceExpert.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) { // Check if the user exists in the expert database
                if (snapshot.exists()) {
                    for (expertUser in snapshot.children) {
                        val userPassword = expertUser.child("password").getValue(String::class.java)
                        if (userPassword == password) { // If the user password is right, set the isExist variable to 1
                            isExist = 1
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
        return isExist
    }
}