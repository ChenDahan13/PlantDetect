package com.example.shroomer

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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.IOException

class FragmentNewPost :Fragment() {
    private lateinit var imageBitmap :Bitmap
    lateinit var post: Post

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_new_post,container,false)

        val buttonCapture = view.findViewById<FloatingActionButton>(R.id.button_capture)
        val buttonGallery = view.findViewById<Button>(R.id.button_gallery)
        val buttonUpload = view.findViewById<Button>(R.id.confirm_button)

        buttonCapture.setOnClickListener{
            dispatchTakePictureIntent()
        }

        buttonGallery.setOnClickListener{
            dispatchOpenGallery()
        }

        buttonUpload.setOnClickListener{
            dispatchUploadPost(view)
        }

        return view
    }


    private fun dispatchTakePictureIntent(){
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

    private val pickImage = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->
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
        //verification contains image and text
        var titleText :String
        titleText = view.findViewById<EditText>(R.id.editPostText).text.toString()
        Log.i("titleText" , titleText)
        Log.i("username" , "someuser")

        if(titleText.isEmpty() || (imageBitmap.width==0 && imageBitmap.height==0)){
            Log.i("POST VERIFICATION", "No Title No Image")
            return
        }

        //creating object

        //uploading

        //finish
        parentFragmentManager.beginTransaction().remove(this).commit()
    }

}