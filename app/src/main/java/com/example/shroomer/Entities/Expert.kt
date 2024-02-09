package com.example.shroomer.Entities

import java.util.LinkedList

class Expert(username: String, email: String, password: String, user_id: String, certificateUri: String) :
    User(username, email, password, user_id) {

        private lateinit var comments: LinkedList<Long>
        private lateinit var likes: LinkedList<Long>
        private lateinit var certificate: String

    init {
        this.comments = LinkedList()
        this.likes = LinkedList()
        this.certificate = certificateUri
    }

    fun createComment(content: String, post: Post) {}
    fun likeComment(comment: Comment) {}
}