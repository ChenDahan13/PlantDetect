package com.example.shroomer

import java.util.LinkedList

class Post {
    private lateinit var title: String
    private lateinit var comments: LinkedList<Long>
    private var user_id: Long = 0

    constructor(title: String, user_id: Long) {
        this.title = title
        this.user_id = user_id
        this.comments = LinkedList()
    }
}