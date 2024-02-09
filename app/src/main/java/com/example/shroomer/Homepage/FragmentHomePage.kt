package com.example.shroomer.Homepage

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.shroomer.Entities.User
import com.example.shroomer.R

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

}