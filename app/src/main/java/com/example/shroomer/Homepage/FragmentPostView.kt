package com.example.shroomer.Homepage

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
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class commentAdapter(context: Context, private val commentList: List<Comment>) : ArrayAdapter<Comment>(context, 0, commentList) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var itemView = convertView
        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.comment_item, parent, false)
        }
        val currentComment = commentList[position]

        // Bind data to views in the layout
        val commentContent = itemView?.findViewById<TextView>(R.id.comment_contents)
        commentContent?.text = currentComment.getContent()

        val commentOwner = itemView?.findViewById<TextView>(R.id.username_who_commented)
        commentOwner?.text = currentComment.getUserId()

        return itemView!!
    }

}

class FragmentPostView : Fragment() {

    private var _binding: FragmentPostViewBinding? = null
    private val binding get() = _binding!!
    private lateinit var postID: String
    private lateinit var userID: String

    // Firebase references
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReferencePost: DatabaseReference
    private lateinit var databaseReferenceComment: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            postID = it.getString("post_id").toString()
            userID = it.getString("user_id").toString()
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

        viewPost()
        showComments()

        binding.submitCommentButton.setOnClickListener {
            val comment = binding.commentInput.text.toString()
            if (comment.isNotEmpty()) {
                addComment(comment)
            } else {
                Toast.makeText(context, "Comment cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    // Show the comment of the post
    private fun showComments() {
        databaseReferenceComment.orderByChild("post_id").equalTo(postID).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val commentList = mutableListOf<Comment>()
                for (commentSnapshot in snapshot.children) {
                    val content = commentSnapshot.child("content").getValue(String::class.java)
                    val user_id = commentSnapshot.child("user_id").getValue(String::class.java)
                    val comment_id = commentSnapshot.child("comment_id").getValue(String::class.java)
                    val post_id = commentSnapshot.child("post_id").getValue(String::class.java)
                    val comment = Comment(comment_id?: "", content?: "", user_id?: "", post_id?: "")
                    comment?.let {
                        commentList.add(it)
                    }
                }
                updateAdapter(commentList)
            }
            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
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
                    binding.postOwnerUsername.text = userId
                    // Load image
                    loadImageFromUrl(imageUrl)

                }
            }
            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })
    }

    // Function to load image from URL
    private fun loadImageFromUrl(imageUrl: String?) {
        Picasso.get().load(imageUrl).into(binding.postImageView)
    }

    // Add a comment to the post
    private fun addComment(comment: String) {
        val commentID = databaseReferenceComment.push().key.toString() // Generate a unique key for the comment
        val newComment = Comment(commentID, comment, userID, postID)
        databaseReferenceComment.child(commentID).setValue(newComment.toMap())
            .addOnSuccessListener { Toast.makeText(context, "Comment added", Toast.LENGTH_SHORT).show() }
            .addOnFailureListener { Toast.makeText(context, "Failed to add comment", Toast.LENGTH_SHORT).show() }
    }

    // Update the adapter with the comments
    private fun updateAdapter(commentList: List<Comment>) {
        val adapter = commentAdapter(requireContext(), commentList)
        val listView = view?.findViewById<ListView>(R.id.comments_list_view)
        listView?.adapter = adapter
    }
}