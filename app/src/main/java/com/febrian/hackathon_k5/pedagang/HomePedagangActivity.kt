package com.febrian.hackathon_k5.pedagang

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.febrian.hackathon_k5.R
import com.febrian.hackathon_k5.databinding.ActivityHomePedagang2Binding
import com.febrian.hackathon_k5.databinding.ActivityHomePedagangBinding

class HomePedagangActivity : AppCompatActivity() {

    private lateinit var binding : ActivityHomePedagang2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomePedagang2Binding.inflate(layoutInflater)
        setContentView(binding.root)


        val navController = findNavController(R.id.nav_host_fragment)

        binding.bottomNavigation.setupWithNavController(navController)
    }
}