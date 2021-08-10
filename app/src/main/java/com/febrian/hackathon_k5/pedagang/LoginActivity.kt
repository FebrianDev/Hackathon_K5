package com.febrian.hackathon_k5.pedagang

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.febrian.hackathon_k5.R
import com.febrian.hackathon_k5.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}