package com.febrian.hackathon_k5.pembeli

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.febrian.hackathon_k5.R
import com.febrian.hackathon_k5.databinding.ActivityHomePembeliBinding

class HomePembeliActivity : AppCompatActivity() {

    private lateinit var binding : ActivityHomePembeliBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomePembeliBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.nav_host_fragment)

        binding.bottomNavigation.setupWithNavController(navController)
    }
}