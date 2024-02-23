package com.example.shroomer.Homepage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.shroomer.Entities.User
import com.example.shroomer.R
import com.example.shroomer.databinding.FragmentMyProfileBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FragmentMyProfile : Fragment() {

    private var _binding: FragmentMyProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReferenceAmateur: DatabaseReference
    private lateinit var databaseReferenceExpert: DatabaseReference


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMyProfileBinding.inflate(inflater, container, false)

        // Create the database reference
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReferenceAmateur = firebaseDatabase.reference.child("Amateur")
        databaseReferenceExpert = firebaseDatabase.reference.child("Expert")

        val myUserID = activity?.intent?.getStringExtra("my_user_id")
        if (myUserID == null) {
            Toast.makeText(context, "User ID is null", Toast.LENGTH_SHORT).show()
            return binding.root
        }

        isAmateurOrExpert(myUserID)

        return binding.root
    }

    private fun isAmateurOrExpert(myUserID: String) {
        databaseReferenceAmateur.orderByChild("user_id").equalTo(myUserID)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (amateurSnapshot in snapshot.children) {
                            val username = amateurSnapshot.child("username").getValue(String::class.java)
                            val email = amateurSnapshot.child("email").getValue(String::class.java)
                            binding.profileUsername.text = username
                            binding.profileEmail.text = email
                            binding.profileLevel.text = "Amateur"
                        }

                    } else {
                        databaseReferenceExpert.orderByChild("user_id").equalTo(myUserID)
                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    if (snapshot.exists()) {
                                        for (expertsnapshot in snapshot.children) {
                                            val username = expertsnapshot.child("username").getValue(String::class.java)
                                            val email = expertsnapshot.child("email").getValue(String::class.java)
                                            binding.profileUsername.text = username
                                            binding.profileEmail.text = email
                                            binding.profileLevel.text = "Expert"
                                        }
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                                }
                            })
                    }
                    }
                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }


}