package com.febrian.hackathon_k5.pembeli

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.febrian.hackathon_k5.databinding.ActivityLoginBinding
import com.febrian.hackathon_k5.databinding.ActivityLoginPembeliBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginPembeliBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginPembeliBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}