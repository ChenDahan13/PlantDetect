package com.example.shroomer.Homepage

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.activity.result.contract.ActivityResultContracts
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.example.shroomer.Entities.Post
import com.example.shroomer.Entities.User
import com.example.shroomer.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.IOException

class FragmentNewPost :Fragment() {
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReferencePost: DatabaseReference
    private var imageBitmap : Bitmap? = null
    private lateinit var post: Post
    private lateinit var myUser: User

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_new_post,container,false)

        // Create the database reference
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReferencePost = firebaseDatabase.reference.child("Post")

        val buttonCapture = view.findViewById<FloatingActionButton>(R.id.button_capture)
        val buttonGallery = view.findViewById<Button>(R.id.button_gallery)
        val buttonUpload = view.findViewById<Button>(R.id.confirm_button)

        buttonCapture.setOnClickListener{
            dispatchTakePictureIntent()
        }

        buttonGallery.setOnClickListener{
            Toast.makeText(context, "Choose from gallery", Toast.LENGTH_SHORT).show()
            dispatchOpenGallery()
        }

        buttonUpload.setOnClickListener{
            dispatchUploadPost(view)
        }

        return view
    }


    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also {
                takePictureIntent-> takePictureLauncher.launch(takePictureIntent)
        }
    }

    private val takePictureLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            imageBitmap = (data?.extras?.get("data") as Bitmap?)!!
            view?.findViewById<ImageView>(R.id.image1view)?.setImageBitmap(imageBitmap)
        }
    }

    private fun dispatchOpenGallery(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type="image/*"
        pickImage.launch(intent)
    }

    private val pickImage = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result->
        if( result.resultCode==Activity.RESULT_OK){
            val selectedImageUri : Uri? = result.data?.data
            selectedImageUri?.let {uri ->
                try {
                    val inputStream = context?.contentResolver?.openInputStream(uri)
                    imageBitmap = BitmapFactory.decodeStream(inputStream)
                }catch (e: IOException){
                    e.printStackTrace()
                    null
                }
            }
            view?.findViewById<ImageView>(R.id.image1view)?.setImageBitmap(imageBitmap)
        }
    }

    private fun dispatchUploadPost(view: View){
        myUser = arguments?.getParcelable<User>("my_user_parcelable")!! // Get the user from the arguments
        // verification contains image and text
        var titleText: String
        titleText = view.findViewById<EditText>(R.id.editPostText).text.toString()
        Log.i("titleText" , titleText)
        Log.i("username" , "${myUser.getUsername()}")

        if(titleText.isEmpty() || imageBitmap == null) {
            Log.i("POST VERIFICATION", "No Title No Image")
            return
        }

        val postID = databaseReferencePost.push().key.toString() // Generate a unique key for the post
        post = Post(titleText, myUser.getUsername(), imageBitmap!!) // Create the post object
        databaseReferencePost.child(postID).setValue(post.toMap()) // Save the post to the database
        Toast.makeText(context, "Post uploaded", Toast.LENGTH_SHORT).show()
        view.findViewById<EditText>(R.id.editPostText).setText("") // Clear the text
        view.findViewById<ImageView>(R.id.image1view).setImageBitmap(null) // Clear the image
        parentFragmentManager.beginTransaction().remove(this).commit()
    }

}