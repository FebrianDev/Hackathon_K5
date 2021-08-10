package com.febrian.hackathon_k5.pedagang

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.febrian.hackathon_k5.R
import com.febrian.hackathon_k5.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}