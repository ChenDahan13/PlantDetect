package com.example.shroomer.Entities

import android.os.Parcel
import android.os.Parcelable
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

    override fun toMap(): Map<String, Any> {
        val userMap = super.toMap()
        return userMap + mapOf(
            "certificate_uri" to certificate
        )
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        super.writeToParcel(parcel, flags)
        parcel.writeString(certificate)
    }
    companion object CREATOR : Parcelable.Creator<Expert>{
        override fun createFromParcel(parcel: Parcel): Expert {
            val expert =User.CREATOR.createFromParcel(parcel)
            val certificateUri=parcel.readString()!!

            return Expert(expert.getUsername(),expert.getEmail(),expert.getPassword(),expert.getUserID(), certificateUri)
        }

        override fun newArray(size: Int): Array<Expert?> {
            return arrayOfNulls(size)
        }
    }
}