package com.example.shroomer

import android.os.Parcel
import android.os.Parcelable
import java.util.LinkedList
import java.util.Queue

open class User (
    private var username: String,
    private var email: String,
    private var password: String,
    private var user_id: String
) :Parcelable{

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
    fun getUsername(): String {return this.username}
    fun getEmail(): String {return this.email}

    fun getUserID(): String{return this.user_id}

    fun login(name: String, password: String) {}
    fun editProfile(other: User) {}



    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    ) {
        posts_ids= LinkedList<Long>()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(username)
        parcel.writeString(email)
        parcel.writeString(password)
        parcel.writeString(user_id)
        parcel.writeLongArray(posts_ids.toLongArray())
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }


}