package com.example.shroomer.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.shroomer.Entities.Amateur
import com.example.shroomer.Entities.User
import com.example.shroomer.Entities.Expert
import com.example.shroomer.Homepage.HomePageAmateur
import com.example.shroomer.R
import com.example.shroomer.databinding.ActivityMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginActivity : AppCompatActivity() {
    // Set the database reference
    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReferenceAmateur: DatabaseReference
    private lateinit var databaseReferenceExpert: DatabaseReference
    private lateinit var myUser: User
    private lateinit var extraExpertAmateur : String
    private lateinit var usernameExtra : String
    private lateinit var userid : String

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
            isUserExist(username, password) { isExist ->
                if (isExist) {
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                    val homePageIntent = Intent(this, HomePageAmateur::class.java)
                    homePageIntent.putExtra("my_user_id", userid)
                    homePageIntent.putExtra("username", usernameExtra)
                    startActivity(homePageIntent)
                } else {
                    Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Clicking on the sign up button
        signupButton.setOnClickListener {
            val signUpIntent = Intent(this, SignUpActivity::class.java)
            startActivity(signUpIntent)
        }
    }

    private fun isUserExist(username:String, password: String, callback: (Boolean) -> Unit) {
        var isExist = false
        databaseReferenceAmateur.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) { // Check if the user exists in the amateur database
                if (snapshot.exists()) {
                    for (amateurUser in snapshot.children) {
                        val userPassword = amateurUser.child("password").getValue(String::class.java)
                        if (userPassword == password) { // If the user password is right, set the isExist variable to 1
                            isExist = true
                            usernameExtra = amateurUser.child("username").getValue(String::class.java).toString()
                            userid = amateurUser.key.toString()
                            callback(isExist)
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@LoginActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })

        databaseReferenceExpert.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) { // Check if the user exists in the expert database
                if (snapshot.exists()) {
                    for (expertUser in snapshot.children) {
                        val userPassword = expertUser.child("password").getValue(String::class.java)
                        if (userPassword == password) { // If the user password is right, set the isExist variable to 1
                            isExist = true
                            usernameExtra = expertUser.child("username").getValue(String::class.java).toString()
                            userid = expertUser.key.toString()
                            callback(isExist)
                        } else { // Password is wrong
                            callback(isExist)
                        }
                    }
                } else {
                    callback(isExist)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@LoginActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                callback(isExist)
            }
        })
    }
}