package com.example.shroomer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast

class SignUpPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_page)

        // Access the items of the list
        val users_options = resources.getStringArray(R.array.Users)

        // Access the spinner
        val spinner: Spinner = findViewById(R.id.spinner)
        if (spinner != null) {
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, users_options)
            spinner.adapter = adapter

           /* spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val selectedUser = users_options[position]
                    if (selectedUser.equals("Expert")) {
                        Toast.makeText(this,"Upload certificate", Toast.LENGTH_SHORT).show()
                    }
                }
            } */
        } else {
            println("Error to access the spinner")
        }

    }
}