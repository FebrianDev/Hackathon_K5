package com.febrian.hackathon_k5.pedagang

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.febrian.hackathon_k5.MainActivity
import com.febrian.hackathon_k5.R
import com.febrian.hackathon_k5.databinding.FragmentHomePedagangBinding
import com.google.android.gms.location.*
import com.google.firebase.database.*


class HomePedagangFragment : Fragment() {

    private lateinit var binding: FragmentHomePedagangBinding
    var active = false
    private lateinit var database: DatabaseReference

    companion object {
        const val KEY_NAME = "KEY_NAME"
    }

    //    private lateinit var locationCallback : LocationCallback
//
//    private lateinit var locationRequest : LocationRequest
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null


    private val REQUEST_LOCATION = 1
    var showLocation: TextView? = null
    var locationManager: LocationManager? = null
    var latitude: String? = null
    var longitude: String? = null

    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomePedagangBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        sharedPreferences = activity?.getSharedPreferences(
            MainActivity.KEYLOGIN,
            AppCompatActivity.MODE_PRIVATE
        )!!

        val name = sharedPreferences.getString(HomeActivity.KEY_NAME, "")
        binding.btnTambah.setOnClickListener {
            val intent = Intent(view.context, AddDagangan::class.java)
            intent.putExtra(HomeActivity.KEY_NAME, name)
            startActivity(intent)
        }

        database =
            FirebaseDatabase.getInstance().getReference("UsersPedagang").child(name.toString())
        database.addValueEventListener(object : ValueEventListener {
            @SuppressLint("SetTextI18n", "ResourceAsColor")
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.loading.visibility = View.GONE
                if (snapshot.child("nama_pemilik").value != null)
                    binding.nama.setText(snapshot.child("nama_pemilik").value.toString())
                if (snapshot.child("dagangan").value != null)
                    binding.dagangan.setText(snapshot.child("dagangan").value.toString())
                if (snapshot.child("wa").value != null)
                    binding.wa.setText(snapshot.child("wa").value.toString())
                if (snapshot.child("deskripsi").value != null)
                    binding.deskripsi.setText(snapshot.child("deskripsi").value.toString())

                if (snapshot.child("url_image").value != null)
                    Glide.with(view.context).load(snapshot.child("url_image").value.toString())
                        .into(binding.imgUser)

                val list = ArrayList<String>()
                for (i in 0 until 3) {
                    if (snapshot.child("img$i").exists()) {
                        list.add(snapshot.child("img$i").value.toString())
                    }
                }

                if (list.size != 0) {
                    binding.linearLayout2.visibility = View.GONE
                    binding.rv.visibility = View.VISIBLE
                    binding.rv.layoutManager = LinearLayoutManager(
                        view.context, LinearLayoutManager.HORIZONTAL,
                        false
                    )
                    binding.rv.adapter = AdapterHomeString(list)
                } else {
                    binding.linearLayout2.visibility = View.VISIBLE
                    binding.rv.visibility = View.GONE
                }

                active = snapshot.child("active").value as Boolean
                if (active == false) {
                    binding.btnOnOff.text = "Off"
                    binding.btnOnOff.setBackgroundResource(R.drawable.bg_btn_red)
                } else {
                    binding.btnOnOff.text = "On"
                    binding.btnOnOff.setBackgroundResource(R.drawable.bg_btn_green)
                }
            }

            //
            override fun onCancelled(error: DatabaseError) {
                binding.loading.visibility = View.GONE
                Toast.makeText(view.context, error.message.toString(), Toast.LENGTH_LONG).show()
            }

        })

        binding.btnOnOff.setOnClickListener {
            active = !active
            if (active == false) {
                binding.btnOnOff.text = "Off"
                binding.btnOnOff.setBackgroundResource(R.drawable.bg_btn_green)
            } else {
                binding.btnOnOff.text = "On"
                binding.btnOnOff.setBackgroundResource(R.drawable.bg_btn_red)
            }

            val databaseUpdate =
                FirebaseDatabase.getInstance().getReference("UsersPedagang").child(name.toString())
            databaseUpdate.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.ref.child("active").setValue(active)
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(view.context, error.message.toString(), Toast.LENGTH_LONG).show()
                }

            })
        }


        LocationServices.getFusedLocationProviderClient(requireActivity())
            .also { fusedLocationProviderClient = it }

        if (ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            requireActivity().let {
                ActivityCompat.requestPermissions(
                    it,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    HomePedagangActivity.REQ
                )
            }

        } else {
            fusedLocationProviderClient?.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            super.onLocationResult(p0)
            try {
                if (active) {
                    database.ref.child("lat").setValue(p0.lastLocation.latitude)
                    database.ref.child("long").setValue(p0.lastLocation.longitude)

                    Toast.makeText(
                        view?.context,
                        "Lat ${p0.lastLocation.latitude} Long ${p0.lastLocation.longitude}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(view?.context, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private val locationRequest = LocationRequest.create().apply {
        interval = 1000
        priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            HomePedagangActivity.REQ -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (activity?.let {
                            ContextCompat.checkSelfPermission(
                                it,
                                Manifest.permission.ACCESS_FINE_LOCATION
                            )
                        } == PackageManager.PERMISSION_GRANTED
                    ) {
                        LocationServices.getFusedLocationProviderClient(view?.context!!.applicationContext)
                            .also { fusedLocationProviderClient = it }
                        fusedLocationProviderClient?.requestLocationUpdates(
                            locationRequest,
                            locationCallback,
                            Looper.getMainLooper()
                        )
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(view?.context, "permission denied", Toast.LENGTH_LONG).show()
                }
                return
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (view?.context?.let {
                ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            }
            == PackageManager.PERMISSION_GRANTED) {

            fusedLocationProviderClient?.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }
    }

    override fun onPause() {
        super.onPause()
        if (view?.context?.let {
                ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            }
            == PackageManager.PERMISSION_GRANTED) {

            fusedLocationProviderClient!!.removeLocationUpdates(locationCallback)
        }

    }
}