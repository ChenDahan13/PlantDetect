package com.example.shroomer

import java.util.LinkedList
import java.util.Queue

open class User(
    private var name: String,
    private var email: String,
    private var password: String,
    private var user_id: Long,
    private var token: Int
) {
    private lateinit var posts_ids: Queue<Long>

    init {
        this.posts_ids = LinkedList()
    }

    fun login(name: String, password: String) {}
    fun editProfile(other: User) {}
}