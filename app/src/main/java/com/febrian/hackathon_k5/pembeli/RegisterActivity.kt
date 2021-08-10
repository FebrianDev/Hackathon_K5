package com.febrian.hackathon_k5.pembeli

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.febrian.hackathon_k5.databinding.ActivityRegisterPembeliBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRegisterPembeliBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterPembeliBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}