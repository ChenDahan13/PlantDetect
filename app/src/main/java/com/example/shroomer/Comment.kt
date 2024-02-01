package com.example.shroomer

import java.util.LinkedList

class Comment {
    private var comment_id: Long = 0
    private lateinit var content: String
    private var user_id: Long = 0
    private lateinit var likedBy: LinkedList<Long>

    constructor(comment_id: Long, content: String, user_id: Long) {
        this.comment_id = comment_id
        this.content = content
        this.user_id = user_id
        this.likedBy = LinkedList()
    }
}