package com.febrian.hackathon_k5.pembeli

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.febrian.hackathon_k5.MainActivity
import com.febrian.hackathon_k5.databinding.ActivityLoginPembeli2Binding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.*

class LoginPembeli2Activity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginPembeli2Binding
    private lateinit var database: DatabaseReference

    private lateinit var sharedPreferences : SharedPreferences

    companion object{
        const val KEY_NAME = "KEY_NAME2"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginPembeli2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences(MainActivity.KEYLOGIN, MODE_PRIVATE)

        binding.register.setOnClickListener {
            val intent = Intent(applicationContext, RegisterPembeli2Activity::class.java)
            startActivity(intent)
        }

        binding.btnMasuk.setOnClickListener {
            val name = binding.nama.text.toString()
            val pwd = binding.password.text.toString()

            database = FirebaseDatabase.getInstance().reference.child("UsersPembeli").child(name)
            if (name.isEmpty()) {
                binding.nama.error = "Username Tidak Boleh Kosong!"
            } else if (pwd.isEmpty()) {
                binding.password.error = "Password Tidak Boleh Kosong!"
            }else{
                binding.btnMasuk.isEnabled = false
                binding.btnMasuk.text = "Loading...."
                database.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if(snapshot.exists()){
                            if(snapshot.child("password").value.toString() == pwd){
                                binding.btnMasuk.isEnabled = true
                                binding.btnMasuk.text = "Masuk"
                                Snackbar.make(binding.root, "Login Berhasil!", Snackbar.LENGTH_SHORT).show()
                                sharedPreferences.edit().apply {
                                    putString(MainActivity.KEYLOGIN, MainActivity.keylogin_pembeli)
                                    putString(KEY_NAME, name)
                                    apply()
                                }

                                val intent = Intent(applicationContext, HomePembeliActivity::class.java)
                                intent.putExtra(KEY_NAME, name)
                                startActivity(intent)
                                finish()
                            }else{
                                binding.btnMasuk.isEnabled = true
                                binding.btnMasuk.text = "Masuk"
                                Snackbar.make(binding.root, "Password Salah!", Snackbar.LENGTH_SHORT).show()
                            }
                        }else{
                            binding.btnMasuk.isEnabled = true
                            binding.btnMasuk.text = "Masuk"
                            Snackbar.make(binding.root, "Username Tidak Ada!", Snackbar.LENGTH_SHORT).show()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        binding.btnMasuk.isEnabled = true
                        binding.btnMasuk.text = "Masuk"
                        Toast.makeText(applicationContext, error.message, Toast.LENGTH_LONG).show()
                    }

                })
            }
        }
    }
}