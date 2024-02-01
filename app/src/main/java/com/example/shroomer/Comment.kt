package com.example.shroomer

import java.util.LinkedList

class Comment(
    private var comment_id: Long,
    private var content: String,
    private var user_id: Long
) {
    private lateinit var likedBy: LinkedList<Long>

    init {
        this.likedBy = LinkedList()
    }
}