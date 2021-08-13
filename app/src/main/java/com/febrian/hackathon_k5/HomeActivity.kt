package com.febrian.hackathon_k5

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.febrian.hackathon_k5.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityHomeBinding

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        actionBar?.setHomeAsUpIndicator(R.drawable.icon_navigate)
//        actionBar?.setDisplayHomeAsUpEnabled(true)
//        actionBar?.hide()
//        supportActionBar?.hide()
//        supportActionBar?.setHomeAsUpIndicator(R.drawable.icon_navigate)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        binding.appBarHome.toolbar.elevation = 0f
//        supportActionBar?.elevation = 0f
//        actionBar?.elevation = 0f
        binding.appBarHome.toolbar.setLogo(R.drawable.icon_navigate)
        setSupportActionBar(binding.appBarHome.toolbar)
        
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_home)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.


        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_home)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}