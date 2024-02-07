package com.example.shroomer

import java.util.LinkedList

class Comment(
    private var comment_id: Long,
    private var content: String,
    private var user_id: Long,
    private var like_count: Int=0
) {
    private lateinit var likedBy: LinkedList<Long>

    init {
        this.likedBy = LinkedList()
    }

    fun Comment.addLike(user_id: Long){
        this.likedBy.add(user_id)
        this.like_count++
    }

    fun Comment.removeLike(user_id: Long){
        if(likedBy.contains(user_id)){
            this.like_count--
            this.likedBy.remove(user_id)
        }
    }
}