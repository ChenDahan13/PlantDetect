package com.example.shroomer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import android.view.View
import com.example.shroomer.databinding.ActivitySignUpPageBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SignUpPage : AppCompatActivity() {

    // Set the database reference
    private lateinit var binding: ActivitySignUpPageBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReferenceAmateur: DatabaseReference
    private lateinit var databaseReferenceExpert: DatabaseReference
    private lateinit var spinner: Spinner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Create the database reference
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReferenceAmateur = firebaseDatabase.reference.child("Amateur")
        databaseReferenceExpert = firebaseDatabase.reference.child("Expert")
        val users_options = resources.getStringArray(R.array.Users)
        // Access the spinner
        spinner = findViewById(R.id.spinner)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, users_options)
        spinner.adapter = adapter
        spinnerChoice()
        binding.signupButton.setOnClickListener {
            // Get the user input
            val username = binding.signupusername.text.toString()
            val email = binding.signupemail.text.toString()
            val password = binding.signuppassword.text.toString()
            val confirmPassword = binding.signupconfirmpassword.text.toString()
            val selectedItem = spinner.selectedItem.toString()
            // Check if the fields are empty
            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // Check if the password and the confirm password are the same
            if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // Check if the user is an amateur or an expert
            if (selectedItem == "Amateur") {
                signUpAmateur(username, email, password)
            } else {
                signUpExpert(username, email, password)
            }
        }
    }

    private fun spinnerChoice() {
        // Access the items of the list
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Toast.makeText(this@SignUpPage, "You selected ${spinner.selectedItem}", Toast.LENGTH_SHORT)
                    .show()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(this@SignUpPage, "Did not select option", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    // Function to sign up an amateur
    private fun signUpAmateur(username: String, email: String, password: String) {
        databaseReferenceAmateur.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) { // If the username already exists
                    Toast.makeText(this@SignUpPage, "Username already exists", Toast.LENGTH_SHORT).show()
                } else { // If the username does not exist
                    val primraryKey = databaseReferenceAmateur.push().key // Primary key for the database
                    var user: User? = null
                    user = Amateur(username, email, password, primraryKey.toString())
                    Toast.makeText(this@SignUpPage, "Amateur user created", Toast.LENGTH_SHORT).show()
                    databaseReferenceAmateur.child(primraryKey!!).setValue(user.toMap()) // Add the user to the database with the primary key
                    startActivity(Intent(this@SignUpPage, MainActivity::class.java)) // Go to the login page
                    finish()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@SignUpPage, "Error creating user", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Function to sign up an expert
    private fun signUpExpert(username: String, email: String, password: String) {
        databaseReferenceExpert.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) { // If the username already exists
                    Toast.makeText(this@SignUpPage, "Username already exists", Toast.LENGTH_SHORT).show()
                } else { // If the username does not exist
                    val primraryKey = databaseReferenceExpert.push().key // Primary key for the database
                    var user: User? = null
                    user = Expert(username, email, password, primraryKey.toString())
                    Toast.makeText(this@SignUpPage, "Expert user created", Toast.LENGTH_SHORT).show()
                    databaseReferenceExpert.child(primraryKey!!).setValue(user.toMap()) // Add the user to the database with the primary key
                    startActivity(Intent(this@SignUpPage, MainActivity::class.java)) // Go to the login page
                    finish()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@SignUpPage, "Error creating user", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
