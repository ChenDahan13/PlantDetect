package com.example.shroomer.Entities

import android.graphics.Bitmap
import java.net.URL
import java.util.LinkedList

class Post (var title: String, var user_id: String , var imageBitmap: String, var post_id: String)
{
    private lateinit var comments: LinkedList<Comment>

    init {
        this.comments = LinkedList()
    }

    open fun toMap(): Map<String, Any> {
        return mapOf(
            "title" to this.title,
            "user_id" to this.user_id,
            "imageBitmap" to this.imageBitmap,
            "post_id" to this.post_id
        )
    }

    fun Post.addComment(comment : Comment){
        this.comments.add(comment)
    }
}
