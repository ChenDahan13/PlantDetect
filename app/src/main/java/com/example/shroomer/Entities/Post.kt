package com.example.shroomer.Entities

import android.graphics.Bitmap
import java.net.URL
import java.util.LinkedList

class Post (var title: String, var user_id: String , var imageBitmap: String, var post_id: String)
{
    private lateinit var comments: LinkedList<String>

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

    fun addComment(comment_id : String){
        this.comments.add(comment_id)
    }

    fun removeComment(comment_id: String){
        if(comments.contains(comment_id)){
            this.comments.remove(comment_id)
        }
    }
}
