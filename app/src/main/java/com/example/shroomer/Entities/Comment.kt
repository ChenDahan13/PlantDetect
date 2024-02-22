package com.example.shroomer.Entities

import java.util.LinkedList

class Comment(
    private var comment_id: String,
    private var content: String,
    private var user_id: String,
    private var post_id: String,
    private var like_count: Int=0
) {
    private lateinit var likedBy: LinkedList<String>

    init {
        this.likedBy = LinkedList()
    }

    fun addLike(user_id: String) {
        if (!likedBy.contains(user_id)) {
                this.likedBy.add(user_id)
                this.like_count++
        }
    }

    fun removeLike(user_id: String){
        if(likedBy.contains(user_id) && this.like_count > 0) {
            this.like_count--
            this.likedBy.remove(user_id)
        }
    }
    open fun toMap(): Map<String, Any> {
        val map = mutableMapOf(
            "comment_id" to this.comment_id,
            "content" to this.content,
            "user_id" to this.user_id,
            "post_id" to this.post_id,
        )
        // Add the likedBy field if it's initialized
        if (::likedBy.isInitialized) {
            map["likedBy"] = likedBy.joinToString{ "," }
        }
        return map
    }
    fun getCommentId(): String {
        return this.comment_id
    }
    fun getContent(): String {
        return this.content
    }
    fun getUserId(): String {
        return this.user_id
    }
    fun getPostId(): String {
        return this.post_id
    }
    fun getLikeCount(): Int {
        return this.like_count
    }

}