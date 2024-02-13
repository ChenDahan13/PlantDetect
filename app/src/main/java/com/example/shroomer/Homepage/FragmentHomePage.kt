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
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.shroomer.Entities.Post
import com.example.shroomer.Entities.User
import com.example.shroomer.R
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

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_home_page,container,false)
        setupViews(view)

        val myUser = arguments?.getParcelable<User>("my_user_parcelable")


        view.findViewById<TextView>(R.id.hello_user1).text="Hello "+myUser?.getUsername()+" !"


        return view
    }

    private fun setupViews(view: View){

    }
    private fun fetchPosts() {
        val db = FirebaseFirestore.getInstance()
        val storageRef = FirebaseStorage.getInstance().reference
        val postsList = mutableListOf<Post>()

        db.collection("posts")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val title = document.getString("title") ?: ""
                    val userId = document.getString("userId") ?: ""
                    val imageUrl = document.getString("imageUrl") ?: ""

                    // Download image from Firebase Storage
                    val imageRef: StorageReference = storageRef.child(imageUrl)
                    imageRef.getBytes(Long.MAX_VALUE).addOnSuccessListener { bytes ->
                        val imageBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                        val post = Post(title, userId, imageUrl)
                        postsList.add(post)
                        updateAdapter(postsList)
                    }.addOnFailureListener { exception ->
                        Log.e(TAG, "Error downloading image", exception)
                    }
                }

                updateAdapter(postsList)
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error fetching posts", exception)
                // Handle error
            }
    }
    private fun updateAdapter(postsList: List<Post>) {
        val adapter = CustomAdapter(requireContext(), postsList)
        val listView = view?.findViewById<ListView>(R.id.posts_list_view)
        listView?.adapter = adapter
    }


}
