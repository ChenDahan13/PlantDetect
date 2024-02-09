package com.example.shroomer.Homepage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.shroomer.Entities.User
import com.example.shroomer.R

class FragmentMyProfile : Fragment(){

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_my_profile,container,false)

        val myUser = arguments?.getParcelable<User>("my_user_parcelable")

        view.findViewById<TextView>(R.id.profile_username).text=myUser?.getUsername()
        view.findViewById<TextView>(R.id.profile_email).text=myUser?.getEmail()
        view.findViewById<TextView>(R.id.profile_level).text="TODO Amatauer_Expert"

        return view
    }

}