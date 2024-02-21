package com.example.shroomer.Homepage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.shroomer.databinding.FragmentPostViewBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class FragmentPostView : Fragment() {

    private var _binding: FragmentPostViewBinding? = null
    private val binding get() = _binding!!
    private lateinit var postID: String

    // Firebase references
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReferencePost: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            postID = it.getString("post_id").toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPostViewBinding.inflate(inflater, container, false)

        // Create the database reference
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReferencePost = firebaseDatabase.reference.child("Post")

        viewPost()


        return binding.root
    }

    // Show the post content
    private fun viewPost() {
        databaseReferencePost.orderByChild("post_id").equalTo(postID).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (postSnapshot in snapshot.children) {
                    val title = postSnapshot.child("title").getValue(String::class.java)
                    val userId = postSnapshot.child("user_id").getValue(String::class.java)
                    val imageUrl = postSnapshot.child("imageBitmap").getValue(String::class.java)

                    // Set the post content
                    binding.postTitleView.text = title
                    binding.postOwnerUsername.text = userId
                    // Load image
                    loadImageFromUrl(imageUrl)

                }
            }
            override fun onCancelled(error: DatabaseError) {
                // Failed to read value

            }
        })
    }
    // Function to load image from URL
    private fun loadImageFromUrl(imageUrl: String?) {
        Picasso.get().load(imageUrl).into(binding.postImageView)
    }
}