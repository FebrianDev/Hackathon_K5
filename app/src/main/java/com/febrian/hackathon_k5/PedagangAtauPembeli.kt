package com.febrian.hackathon_k5

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.febrian.hackathon_k5.databinding.ActivityPedagangAtauPembeliBinding
import com.febrian.hackathon_k5.pedagang.Login2Activity
import com.febrian.hackathon_k5.pembeli.LoginPembeli2Activity

class PedagangAtauPembeli : AppCompatActivity() {

    private lateinit var binding : ActivityPedagangAtauPembeliBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPedagangAtauPembeliBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.pedagang.setOnClickListener {
            val intent = Intent(applicationContext, Login2Activity::class.java)
            startActivity(intent)
        }

        binding.pembeli.setOnClickListener {
            val intent = Intent(applicationContext, LoginPembeli2Activity::class.java)
            startActivity(intent)
        }

    }
}