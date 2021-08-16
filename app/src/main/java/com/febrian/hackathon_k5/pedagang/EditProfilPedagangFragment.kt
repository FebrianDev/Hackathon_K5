package com.febrian.hackathon_k5.pedagang

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.febrian.hackathon_k5.MainActivity
import com.febrian.hackathon_k5.PedagangAtauPembeli
import com.febrian.hackathon_k5.R
import com.febrian.hackathon_k5.databinding.FragmentEditProfilPedagangBinding
import com.febrian.hackathon_k5.pedagang.HomePedagangFragment.Companion.KEY_NAME
import com.febrian.hackathon_k5.pembeli.LoginPembeli2Activity
import com.google.firebase.database.*

class EditProfilPedagangFragment : Fragment() {


    private lateinit var binding: FragmentEditProfilPedagangBinding
    private lateinit var database: DatabaseReference
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEditProfilPedagangBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = view.context.getSharedPreferences(
            MainActivity.KEYLOGIN,
            AppCompatActivity.MODE_PRIVATE
        )

        binding.btnSignOut.setOnClickListener {

            sharedPreferences.edit().clear().apply()

            val intent = Intent(view.context, PedagangAtauPembeli::class.java)
            startActivity(intent)
            activity?.finish()
        }

        val name = sharedPreferences.getString(KEY_NAME, "")

        database =
            FirebaseDatabase.getInstance().reference.child("UsersPedagang").child(name.toString())

        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.nama.setText(snapshot.child("username").value.toString())

                binding.nama.setText(snapshot.child("username").value.toString())
                binding.password.setText(snapshot.child("password").value.toString())

                if (snapshot.child("url_image").value != null)
                    Glide.with(view.context).load(snapshot.child("url_image").value.toString())
                        .into(binding.ivPhoto)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(view.context, error.message, Toast.LENGTH_LONG).show()
            }

        })
    }

}