package com.example.shroomer.Entities

import android.graphics.Bitmap
import java.net.URL
import java.util.LinkedList

class Post(public var title: String, var user_id: String , val imageBitmap: String?) {

    private lateinit var comments: LinkedList<Comment>

    init {
        this.comments = LinkedList()
    }

//    fun toMap(): Map<String, Any> {
//        return mapOf(
//            "title" to this.title,
//            "user_id" to this.user_id,
//            "imageBitmap" to this.imageBitmap
//        )
//    }

    fun Post.addComment(comment : Comment){
        this.comments.add(comment)
    }


}