package com.example.shroomer.Entities

import android.graphics.Bitmap
import java.util.LinkedList

class Post(public var title: String, var user_id: String , val imageBitmap: Bitmap) {

    private lateinit var comments: LinkedList<Comment>

    init {
        this.comments = LinkedList()
    }

    fun Post.addComment(comment : Comment){
        this.comments.add(comment)
    }


}