package com.example.shroomer

import android.graphics.Bitmap
import java.util.LinkedList

class Post(private var title: String, private var user_id: String , private val imageBitmap: Bitmap) {

    private lateinit var comments: LinkedList<Comment>

    init {
        this.comments = LinkedList()
    }

    fun Post.addComment(comment :Comment){
        this.comments.add(comment)
    }



}