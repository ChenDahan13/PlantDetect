package com.example.shroomer.Homepage

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.content.Intent
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.shroomer.Entities.Post
import com.example.shroomer.Entities.User
import com.example.shroomer.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class CustomAdapter(context: Context, private val postsList: List<Post>) : ArrayAdapter<Post>(context, 0, postsList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var itemView = convertView
        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        }
        val currentPost = postsList[position]

        // Bind data to views in the layout
        val postPhotoImageView = itemView?.findViewById<CircleImageView>(R.id.postlistphoto)
//        postPhotoImageView?.setImageBitmap(currentPost.imageBitmap)
        loadImageFromUrl(currentPost.imageBitmap, postPhotoImageView)

        val titleTextView = itemView?.findViewById<TextView>(R.id.title)
        titleTextView?.text = currentPost.title

        val usernameTextView = itemView?.findViewById<TextView>(R.id.username)
        usernameTextView?.text = currentPost.user_id  // Assuming you want to display the user ID

        return itemView!!
    }

    private fun loadImageFromUrl(url: String, imageView: ImageView?) {
        // Implement your logic to load image from URL using Picasso, Glide, or any other image loading library
        // Example using Picasso:
        Picasso.get().load(url).into(imageView)
    }
}



class FragmentHomePage :Fragment() {
    private lateinit var databaseReferencePost: DatabaseReference
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_home_page,container,false)
        setupViews(view)
        super.onCreate(savedInstanceState)


//        val myUser = arguments?.getParcelable<User>("my_user_parcelable")
        val myUserID = activity?.intent?.getStringExtra("my_user_id")
        val myUsername = activity?.intent?.getStringExtra("username")
        Toast.makeText(context, "Hello "+myUsername, Toast.LENGTH_SHORT).show()
        fetchPosts()

        view.findViewById<TextView>(R.id.hello_user1).text="Hello "+myUserID+" !"
        return view
    }

    private fun setupViews(view: View){

    }
    private fun fetchPosts() {
        val db = FirebaseFirestore.getInstance()
        val postsList = mutableListOf<Post>()
        databaseReferencePost = FirebaseDatabase.getInstance().getReference("Post")
        val postRef = FirebaseDatabase.getInstance().getReference("Post")
        postRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val postTitleList = mutableListOf<String>()
                val postList = mutableListOf<Post>()
                for (postSnapshot in dataSnapshot.children) {
                    val title = postSnapshot.child("title").getValue(String::class.java)
                    val userId = postSnapshot.child("user_id").getValue(String::class.java)
                    val imageUrl = postSnapshot.child("imageBitmap").getValue(String::class.java)
                    val postID = postSnapshot.child("post_id").getValue(String::class.java)
                    val post = Post(title ?: "", userId ?: "", imageUrl ?: "", postID ?: "")
                    post?.let {
                        postsList.add(it)
                    }
                    title?.let {
                        postTitleList.add(it)
                    }
                }
                updateAdapter(postsList)
                Toast.makeText(requireContext(), "Retrieved ${postsList.size} posts", Toast.LENGTH_SHORT).show()
                // Example: Displaying usernames in Logcat
                for (title in postTitleList) {
                    Log.d("post title", title)
                }
            }


            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                // ...
            }
        })
    }



//        db.collection("Post")
//            .get()
//            .addOnSuccessListener { result ->
//                for (document in result) {
//                    val title = document.getString("title") ?: ""
//                    val userId = document.getString("user_id") ?: ""
//                    val imageUrl = document.getString("imageBitmap") ?: ""
//                    val postID = document.getString("post_id") ?: ""
//                    val post = Post(title, userId, imageUrl, postID)
//                    postsList.add(post)
//                }
//                val length = postsList.size
//                Toast.makeText(requireContext(), "Retrieved $length posts", Toast.LENGTH_SHORT).show()
//
//                // Update adapter with postsList after fetching all posts
//                updateAdapter(postsList)
//            }
//            .addOnFailureListener { exception ->
//                Log.e(TAG, "Error fetching posts", exception)
//                Toast.makeText(requireContext(), "Error fetching posts", Toast.LENGTH_SHORT).show()
//                // Handle error
//            }
//    }
    private fun updateAdapter(postsList: List<Post>) {
        val adapter = CustomAdapter(requireContext(), postsList)
        val listView = view?.findViewById<ListView>(R.id.posts_list_view)
        listView?.adapter = adapter
    }


}
