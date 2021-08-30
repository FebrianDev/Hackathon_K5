package com.febrian.hackathon_k5.pedagang

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.febrian.hackathon_k5.databinding.ActivityHomePedagangBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding : ActivityHomePedagangBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomePedagangBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}