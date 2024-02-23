package com.example.shroomer.Homepage

import android.content.ComponentCallbacks
import android.widget.ArrayAdapter
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.shroomer.Entities.Comment
import com.example.shroomer.Entities.User
import com.example.shroomer.R
import com.example.shroomer.databinding.FragmentPostViewBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import java.util.LinkedList

class commentAdapter(context: Context, private val commentList: List<Comment>) : ArrayAdapter<Comment>(context, 0, commentList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var itemView = convertView
        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.comment_item, parent, false)
        }
        val currentComment = commentList[position]

        // Bind data to views in the layout content
        val commentContent = itemView?.findViewById<TextView>(R.id.comment_contents)
        commentContent?.text = currentComment.getContent()
        // Bind data to views in the layout owner
        val commentOwner = itemView?.findViewById<TextView>(R.id.username_who_commented)
        getUsername(currentComment.getUserId()) { username ->
            commentOwner?.text = username
        }
        // Set the comment ID as the tag for the like icon
        val likeIcon = itemView?.findViewById<TextView>(R.id.number_of_likes)
        likeIcon?.tag = currentComment.getCommentId()

        return itemView!!
    }

    private fun getUsername(user_id: CharSequence, callback: (String) -> Unit) {
        val firebaseDatabase = FirebaseDatabase.getInstance()
        val databaseReferenceA = firebaseDatabase.reference.child("Amateur")
        val databaseReferenceE = firebaseDatabase.reference.child("Expert")

        databaseReferenceA.orderByChild("user_id").equalTo(user_id.toString())
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (amateurSnapshot in snapshot.children) {
                            val username = amateurSnapshot.child("username").getValue(String::class.java).toString()
                            callback(username)
                            return
                        }
                    } else {
                        databaseReferenceE.orderByChild("user_id").equalTo(user_id.toString())
                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    if (snapshot.exists()) {
                                        for (expertSnapshot in snapshot.children) {
                                            val username = expertSnapshot.child("username").getValue(String::class.java).toString()
                                            callback(username)
                                            return
                                        }
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    // Failed to read value
                                    Toast.makeText(context, "Failed to read user", Toast.LENGTH_SHORT).show()
                                }
                            })
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                    Toast.makeText(context, "Failed to read user", Toast.LENGTH_SHORT).show()
                }
            })
    }

}

class FragmentPostView : Fragment() {

    private var _binding: FragmentPostViewBinding? = null
    private val binding get() = _binding!!
    private lateinit var postID: String
    private lateinit var userOfPostID: String

    // Firebase references
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReferencePost: DatabaseReference
    private lateinit var databaseReferenceComment: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            postID = it.getString("post_id").toString()
            userOfPostID = it.getString("user_id").toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPostViewBinding.inflate(inflater, container, false)

        // Create the database reference
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReferencePost = firebaseDatabase.reference.child("Post")
        databaseReferenceComment = firebaseDatabase.reference.child("Comment")

        val myUser_id = activity?.intent?.getStringExtra("my_user_id")

        viewPost()
        showComments()

        binding.submitCommentButton.setOnClickListener {
            val comment = binding.commentInput.text.toString()
            if (comment.isNotEmpty()) {
                addComment(comment, myUser_id)
            } else {
                Toast.makeText(context, "Comment cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    // Show the comment of the post
    private fun showComments() {
        databaseReferenceComment.orderByChild("post_id").equalTo(postID)
            .addValueEventListener(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val commentList = mutableListOf<Comment>()
                    for (commentSnapshot in snapshot.children) {
                        val content = commentSnapshot.child("content").getValue(String::class.java)
                        val user_id = commentSnapshot.child("user_id").getValue(String::class.java)
                        val comment_id =
                            commentSnapshot.child("comment_id").getValue(String::class.java)
                        val post_id = commentSnapshot.child("post_id").getValue(String::class.java)
                        val comment =
                            Comment(comment_id ?: "", content ?: "", user_id ?: "", post_id ?: "")
                        comment?.let {
                            commentList.add(it)
                        }
                    }
                    updateAdapter(commentList)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                    Toast.makeText(context, "Failed to read comments", Toast.LENGTH_SHORT).show()
                }
            })
    }

    // Show the post content
    private fun viewPost() {
        databaseReferencePost.orderByChild("post_id").equalTo(postID).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (postSnapshot in snapshot.children) {
                    val title = postSnapshot.child("title").getValue(String::class.java)
                    val userId = postSnapshot.child("user_id").getValue(String::class.java)
                    val imageUrl = postSnapshot.child("imageBitmap").getValue(String::class.java)

                    // Set the post content
                    binding.postTitleView.text = title
                    // Set the post owner
                    getUsername(userId.toString()) { username ->
                        binding.postOwnerUsername.text = username
                    }
                    // Load image
                    loadImageFromUrl(imageUrl)

                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Toast.makeText(context, "Failed to read post", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Function to load image from URL
    private fun loadImageFromUrl(imageUrl: String?) {
        Picasso.get().load(imageUrl).into(binding.postImageView)
    }

    // Add a comment to the post
    private fun addComment(comment: String, myUser_id: String?) {
        val commentID = databaseReferenceComment.push().key.toString() // Generate a unique key for the comment
        val user_id_comment_creator = myUser_id
        if (user_id_comment_creator == null) {
            Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }
        Toast.makeText(context, "User ID: $user_id_comment_creator", Toast.LENGTH_SHORT).show()
        val newComment = Comment(commentID, comment, user_id_comment_creator, postID)
        databaseReferenceComment.child(commentID).setValue(newComment.toMap())
            .addOnSuccessListener {
                Toast.makeText(context, "Comment added", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(
                    context,
                    "Failed to add comment",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun getUsername(user_id: String, callback: (String) -> Unit) {
        val firebaseDatabase = FirebaseDatabase.getInstance()
        val databaseReferenceA = firebaseDatabase.reference.child("Amateur")
        val databaseReferenceE = firebaseDatabase.reference.child("Expert")

        databaseReferenceA.orderByChild("user_id").equalTo(user_id.toString())
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (amateurSnapshot in snapshot.children) {
                            val username = amateurSnapshot.child("username").getValue(String::class.java).toString()
                            callback(username)
                            return
                        }
                    } else {
                        databaseReferenceE.orderByChild("user_id").equalTo(user_id.toString())
                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    if (snapshot.exists()) {
                                        for (expertSnapshot in snapshot.children) {
                                            val username = expertSnapshot.child("username").getValue(String::class.java).toString()
                                            callback(username)
                                            return
                                        }
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    // Failed to read value
                                    Toast.makeText(context, "Failed to read user", Toast.LENGTH_SHORT).show()
                                }
                            })
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                    Toast.makeText(context, "Failed to read user", Toast.LENGTH_SHORT).show()
                }
            })
    }

    // Update the adapter with the comments
    private fun updateAdapter(commentList: List<Comment>) {
        val adapter = commentAdapter(requireContext(), commentList)
        val listView = view?.findViewById<ListView>(R.id.comments_list_view)
        listView?.adapter = adapter
    }

//    private fun incrementLikes(comment_id: String) {
//        val myUserId = activity?.intent?.getStringExtra("my_user_id")
//        var numberOfLikes = view?.findViewById<TextView>(R.id.number_of_likes)
//        if (myUserId == null) {
//            Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show()
//            return
//        }
//        databaseReferenceComment.orderByChild("comment_id").equalTo(comment_id)
//            .addListenerForSingleValueEvent(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    for (commentSnapshot in snapshot.children) {
//                        val comment = commentSnapshot.getValue(Comment::class.java)
//                        if (comment != null) {
//                            comment.addLike(myUserId)
//                            numberOfLikes?.text = comment.getLikeCount().toString()
//                            databaseReferenceComment.child(comment_id).setValue(comment.toMap())
//                                .addOnSuccessListener { Toast.makeText(context, "Like added", Toast.LENGTH_SHORT).show() }
//                                .addOnFailureListener { Toast.makeText(context, "Failed to add like", Toast.LENGTH_SHORT).show() }
//                        }
//                    }
//                }
//                override fun onCancelled(error: DatabaseError) {
//                    // Failed to read value
//                    Toast.makeText(context, "Failed to read comment", Toast.LENGTH_SHORT).show()
//                }
//            })
//    }
//
//    // Increment the number of likes for the post
//    fun incrementLikes(view: View) {
//        val  comment_id = view.tag.toString()
//        incrementLikes(comment_id)
//    }
}