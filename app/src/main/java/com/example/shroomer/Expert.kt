package com.example.shroomer

import java.util.LinkedList

class Expert(name: String, email: String, password: String, user_id: Long, token: Int) :
    User(name, email, password, user_id, token) {

        private lateinit var comments: LinkedList<Long>
        private lateinit var likes: LinkedList<Long>

    init {
        this.comments = LinkedList()
        this.likes = LinkedList()
    }

    fun createComment(content: String, post: Post) {}
    fun likeComment(comment: Comment) {}
}