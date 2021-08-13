package com.febrian.hackathon_k5

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.febrian.hackathon_k5.MainActivity
import com.febrian.hackathon_k5.databinding.ActivityMainBinding
import com.febrian.hackathon_k5.databinding.ActivityRegisterPembeliBinding
import com.febrian.hackathon_k5.pembeli.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val userId = intent.getStringExtra("user_id")
        val emailId = intent.getStringExtra("email_id")

        binding.tampilEmail.text = "User email : $emailId"
        binding.tampilID.text = "User ID : $userId"

        binding.logoutButton.setOnClickListener(){
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            finish()
        }
    }
}