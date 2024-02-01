package com.example.shroomer

import java.util.LinkedList

class Post(private var title: String, private var user_id: Long) {

    private lateinit var comments: LinkedList<Long>

    init {
        this.comments = LinkedList()
    }
}