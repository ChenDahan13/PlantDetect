package com.example.shroomer

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable
import java.util.LinkedList
import java.util.Queue

open class User (
    private var username: String,
    private var email: String,
    private var password: String,
    private var user_id: String
){

    private lateinit var posts_ids: Queue<Long>



    init {
        this.posts_ids = LinkedList()
    }
    fun toMap(): Map<String, Any> {
        return mapOf(
            "username" to this.username,
            "email" to this.email,
            "password" to this.password,
            "user_id" to this.user_id
        )
    }
    fun getPassword(): String { return this.password }
    fun login(name: String, password: String) {}
    fun editProfile(other: User) {}


}