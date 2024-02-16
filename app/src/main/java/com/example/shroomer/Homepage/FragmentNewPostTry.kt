package com.example.shroomer.Homepage

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.shroomer.Entities.Post
import com.example.shroomer.databinding.FragmentNewPostTryBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class FragmentNewPostTry : Fragment() {

    private var _binding: FragmentNewPostTryBinding? = null
    private val binding get() = _binding!!

    // Firebase authentication instance
    private val auth = FirebaseAuth.getInstance()

    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReferencePost: DatabaseReference
    private lateinit var storageRef: StorageReference
    private var imguri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewPostTryBinding.inflate(inflater, container, false)

        // Create the database reference
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReferencePost = firebaseDatabase.reference.child("Post")
        storageRef = FirebaseStorage.getInstance().getReference("Images")

        // Click on confirm upload post button
        binding.confirmButtonTry.setOnClickListener{
            UploadPost()
        }

        // Choose image from gallery
        val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) {
            binding.image1viewTry.setImageURI(it)
            if (it != null) {
                imguri = it
            }
        }

        // Click on gallery button
        binding.buttonGalleryTry.setOnClickListener{
            pickImage.launch("image/*")
        }

        return binding.root
    }

    private fun UploadPost() {

        val title = binding.textBox1Try.text.toString()
        if (title.isEmpty()) {
            binding.textBox1Try.error = "Description is required"
            return
        }
        val postID = databaseReferencePost.push().key!!.toString() // Generate a unique key for the post
        var post: Post? = null

        var userID: String? = null
        val currentUser = auth.currentUser

        // Check if the user is logged in
        if (currentUser != null) {
            userID = currentUser.uid
            Toast.makeText(context, "User ID: $userID", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show()
        }

        imguri?.let {
            storageRef.child(postID).putFile(it)
                .addOnSuccessListener {task ->
                    task.metadata!!.reference!!.downloadUrl
                        .addOnSuccessListener { url ->
                            Toast.makeText(context, "Image uploaded successfully", Toast.LENGTH_SHORT).show()

                            // Get the download URL
                            val imgUrl = url.toString()
                            post = Post(title, "TEST", imgUrl, postID) // TODO!! - Get the userID from the user object

                            // Add the post to the database
                            databaseReferencePost.child(postID).setValue(post?.toMap())
                                .addOnSuccessListener {
                                    Toast.makeText(context, "Post uploaded successfully", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(context, "Failed to upload post", Toast.LENGTH_SHORT).show()
                                }
                        }
                }
        }

    }
}