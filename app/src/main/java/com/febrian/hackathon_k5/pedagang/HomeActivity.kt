package com.febrian.hackathon_k5.pedagang

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.febrian.hackathon_k5.MainActivity.Companion.KEYLOGIN
import com.febrian.hackathon_k5.databinding.ActivityHomePedagangBinding
import com.google.firebase.database.*

class HomeActivity : AppCompatActivity() {

    private lateinit var binding : ActivityHomePedagangBinding
    private lateinit var database: DatabaseReference

    companion object{
        const val KEY_NAME = "KEY_NAME"
    }
    private lateinit var sharedPreferences : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomePedagangBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences(KEYLOGIN, MODE_PRIVATE)

        val name = sharedPreferences.getString(KEY_NAME, "")
        binding.nama.text = name
        binding.btnTambah.setOnClickListener {
            val intent = Intent(applicationContext, AddDagangan::class.java)
            intent.putExtra(KEY_NAME, name)
            startActivity(intent)
        }

        database = FirebaseDatabase.getInstance().getReference("UsersPedagang").child(name.toString())
        database.addListenerForSingleValueEvent(object : ValueEventListener{
            @SuppressLint("SetTextI18n")
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.child("nama_pemilik").value != null)
                    binding.nama.setText(snapshot.child("nama_pemilik").value.toString())
                if(snapshot.child("dagangan").value != null)
                    binding.dagangan.setText(snapshot.child("dagangan").value.toString())
                if(snapshot.child("wa").value != null)
                    binding.wa.setText(snapshot.child("wa").value.toString())
                if(snapshot.child("deskripsi").value != null)
                    binding.deskripsi.setText(snapshot.child("deskripsi").value.toString())

              //  if(snapshot.child("url_image").value != null)
                    Glide.with(applicationContext).load(snapshot.child("url_image").value.toString()).into(binding.imgUser)

                val list = ArrayList<String>()
                for(i in 0 until 3){
                    if(snapshot.child("img$i").exists()){
                        list.add(snapshot.child("img$i").value.toString())
                    }
                }

                if(list.size != 0){
                    binding.linearLayout2.visibility = View.GONE
                    binding.rv.visibility = View.VISIBLE
                    binding.rv.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL,
                        false)
                    binding.rv.adapter = AdapterHomeString(list)
                }else{
                    binding.linearLayout2.visibility = View.VISIBLE
                    binding.rv.visibility = View.GONE
                }
            }
//
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, error.message.toString(), Toast.LENGTH_LONG).show()
            }

        })

    }
}