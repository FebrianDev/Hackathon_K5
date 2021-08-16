package com.febrian.hackathon_k5.pedagang

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.febrian.hackathon_k5.R
import com.febrian.hackathon_k5.databinding.ActivityHomePedagang2Binding
import com.febrian.hackathon_k5.databinding.ActivityHomePedagangBinding
import com.google.android.gms.location.*

class HomePedagangActivity : AppCompatActivity() {

    private lateinit var binding : ActivityHomePedagang2Binding

//    private lateinit var locationCallback : LocationCallback
//
//    private lateinit var locationRequest : LocationRequest
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null

    companion object {
        const val REQ = 101
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            super.onLocationResult(p0)
            try {
//
//                    if (active) {
//                        database.ref.child("lat").setValue(p0.lastLocation.latitude)
//                        database.ref.child("long").setValue(p0.lastLocation.longitude)

                Toast.makeText(
                    applicationContext,
                    "Lat ${p0.lastLocation.latitude} Long ${p0.lastLocation.longitude}",
                    Toast.LENGTH_LONG
                ).show()
                //  }
            } catch (e: Exception) {
                Toast.makeText(applicationContext, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private val locationRequest = LocationRequest.create().apply {
        interval = 1000
        priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomePedagang2Binding.inflate(layoutInflater)
        setContentView(binding.root)


        val navController = findNavController(R.id.nav_host_fragment)

        binding.bottomNavigation.setupWithNavController(navController)


//        LocationServices.getFusedLocationProviderClient(this)
//            .also { fusedLocationProviderClient = it }
//
//        if (ActivityCompat.checkSelfPermission(
//                applicationContext,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                applicationContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
//            && ActivityCompat.checkSelfPermission(
//                applicationContext, Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//            this@HomePedagangActivity.let {
//                ActivityCompat.requestPermissions(
//                    it,
//                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
//                    REQ
//                )
//            }
//
//        }else {
//            fusedLocationProviderClient?.requestLocationUpdates(
//                locationRequest,
//                locationCallback,
//                Looper.getMainLooper()
//            )
//        }
    }

//    private fun checkLocationPermission() {
//        if (applicationContext?.let {
//                ActivityCompat.checkSelfPermission(
//                    it,
//                    Manifest.permission.ACCESS_FINE_LOCATION
//                )
//            } != PackageManager.PERMISSION_GRANTED
//        ) {
//            // Should we show an explanation?
//            if (this@HomePedagangActivity.let {
//                    ActivityCompat.shouldShowRequestPermissionRationale(
//                        it ,
//                        Manifest.permission.ACCESS_FINE_LOCATION
//                    )
//                }
//            ) {
//                // Show an explanation to the user *asynchronously* -- don't block
//                // this thread waiting for the user's response! After the user
//                // sees the explanation, try again to request the permission.
//                AlertDialog.Builder(applicationContext)
//                    .setTitle("Location Permission Needed")
//                    .setMessage("This app needs the Location permission, please accept to use location functionality")
//                    .setPositiveButton(
//                        "OK"
//                    ) { _, _ ->
//                        //Prompt the user once explanation has been shown
//                        requestLocationPermission()
//                    }
//                    .create()
//                    .show()
//            } else {
//                // No explanation needed, we can request the permission.
//                requestLocationPermission()
//            }
//        }
//    }
//
//    private fun requestLocationPermission() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            this@HomePedagangActivity.let {
//                ActivityCompat.requestPermissions(
//                    it,
//                    arrayOf(
//                        Manifest.permission.ACCESS_FINE_LOCATION,
//                        Manifest.permission.ACCESS_BACKGROUND_LOCATION
//                    ),
//                    REQ
//                )
//            }
//        } else {
//            this@HomePedagangActivity.let {
//                ActivityCompat.requestPermissions(
//                    it,
//                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
//                    REQ
//                )
//            }
//        }
//    }

//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//
//        when (requestCode) {
//            REQ -> {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                    // permission was granted, yay! Do the
//                    // location-related task you need to do.
//                    if (this@HomePedagangActivity.let {
//                            ContextCompat.checkSelfPermission(
//                                it,
//                                Manifest.permission.ACCESS_FINE_LOCATION
//                            )
//                        } == PackageManager.PERMISSION_GRANTED
//                    ) {
//                        LocationServices.getFusedLocationProviderClient(this)
//                            .also { fusedLocationProviderClient = it }
//                        fusedLocationProviderClient?.requestLocationUpdates(
//                            locationRequest,
//                            locationCallback,
//                            Looper.getMainLooper()
//                        )
//                    }
//
//                } else {
//
//                    // permission denied, boo! Disable the
//                    // functionality that depends on this permission.
//                    Toast.makeText(applicationContext, "permission denied", Toast.LENGTH_LONG).show()
//                }
//                return
//            }
//        }
//    }
//
//    override fun onResume() {
//        super.onResume()
//        if (applicationContext?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION) }
//            == PackageManager.PERMISSION_GRANTED) {
//            LocationServices.getFusedLocationProviderClient(this)
//                .also { fusedLocationProviderClient = it }
//            fusedLocationProviderClient?.requestLocationUpdates(
//                locationRequest,
//                locationCallback,
//                Looper.getMainLooper()
//            )
//        }
//    }
//
//    override fun onPause() {
//        super.onPause()
//        if (this@HomePedagangActivity.let {
//                ContextCompat.checkSelfPermission(
//                    it,
//                    Manifest.permission.ACCESS_FINE_LOCATION
//                )
//            }
//            == PackageManager.PERMISSION_GRANTED) {
//
//            fusedLocationProviderClient?.removeLocationUpdates(locationCallback)
//        }
//    }
}