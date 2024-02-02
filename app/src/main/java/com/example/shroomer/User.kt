package com.example.shroomer

import java.util.LinkedList
import java.util.Queue

open class User(
    private var username: String,
    private var email: String,
    private var password: String,
    private var user_id: String
) {
    private lateinit var posts_ids: Queue<Long>

    init {
        this.posts_ids = LinkedList()
    }

    fun getPassword(): String { return this.password }
    fun login(name: String, password: String) {}
    fun editProfile(other: User) {}
}