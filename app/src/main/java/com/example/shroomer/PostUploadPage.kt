package com.example.shroomer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.shroomer.databinding.ActivityNewPostBinding
import android.app.Activity
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
import android.graphics.Bitmap
import android.media.Image
import android.provider.ContactsContract.CommonDataKinds.Im
import android.util.Log
import android.widget.Button
import android.widget.ImageView


class PostUploadPage :AppCompatActivity() {

    private lateinit var binding: ActivityNewPostBinding
    lateinit var imageBitmap :Bitmap
    lateinit var post: Post

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = ActivityNewPostBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.buttonCapture.setOnClickListener{
            dispatchTakePictureIntent()
        }

        binding.buttonGallery.setOnClickListener{
            dispatchOpenGallery()
        }

        binding.confirmButton.setOnClickListener{
            dispatchUploadPost()
        }

    }

    private val takePictureLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            imageBitmap = (data?.extras?.get("data") as Bitmap?)!!
            binding.image1view.setImageBitmap(imageBitmap)
        }
    }

    private fun dispatchOpenGallery(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type="image/*"
        pickImage.launch(intent)

    }

    private val pickImage = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->
            if( result.resultCode==Activity.RESULT_OK){
                val selectedImage = result.data!!.data
            }
    }
    private fun dispatchTakePictureIntent(){
        Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE).also {
           takePictureIntent-> takePictureLauncher.launch(takePictureIntent)
        }
    }

    private fun dispatchUploadPost(){
        //verification contains image and text
        var titleText :String
        titleText = binding.textBox1.text.toString()
        //val userName :String= intent.getStringExtra("userid").toString()
        Log.i("titleText" , titleText)
        Log.i("username" , userName.toString())

        if(titleText.isEmpty() || (imageBitmap.width==0 && imageBitmap.height==0)){
            return
        }

        //creating object

        //uploading

        //finish
        finish()
    }


}