package com.febrian.hackathon_k5.pedagang

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.MimeTypeMap
import android.widget.Toast
import com.bumptech.glide.Glide
import com.febrian.hackathon_k5.MainActivity.Companion.KEYLOGIN
import com.febrian.hackathon_k5.MainActivity.Companion.keylogin_pedagang
import com.febrian.hackathon_k5.databinding.ActivityRegister2Binding
import com.febrian.hackathon_k5.pedagang.HomeActivity.Companion.KEY_NAME
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class Register2Activity : AppCompatActivity() {

    private lateinit var binding: ActivityRegister2Binding
    private lateinit var database: DatabaseReference
    private lateinit var storage: StorageReference
    private var uri: Uri? = null

    companion object {
        const val PICK_IMAGE_REQUEST = 1
    }

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegister2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences(KEYLOGIN, MODE_PRIVATE)

        binding.addPhoto.setOnClickListener {
            findImage()
        }

        binding.login.setOnClickListener {
            val intent = Intent(applicationContext, Login2Activity::class.java)
            startActivity(intent)
        }

        binding.btnDaftar.setOnClickListener {
            val name = binding.nama.text.toString()
            val pwd = binding.password.text.toString()

            database = FirebaseDatabase.getInstance().reference.child("UsersPedagang").child(name)
            storage = FirebaseStorage.getInstance().reference.child("ImageUsers")
            val newImage = arrayOfNulls<String>(1)

            if (name.isEmpty()) {
                binding.nama.error = "Username Harus Diisi!"
            } else if (pwd.isEmpty()) {
                binding.password.error = "Password Harus Diisi!"
            } else if (pwd.length < 6) {
                binding.password.error = "Password Harus Minimal 6"
            } else {
                binding.btnDaftar.isEnabled = false
                binding.btnDaftar.text = "Loading...."
                if (uri != null) {
                    val storageReference =
                        storage.child(
                            System.currentTimeMillis().toString() + "." + getFileExtension(uri)
                        )
                    storageReference.putFile(uri!!).addOnSuccessListener {
                        storageReference.downloadUrl.addOnSuccessListener { uri ->
                            newImage[0] = uri.toString()

                            database.addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) =
                                    if (snapshot.exists()) {
                                        binding.nama.error = "Username Sudah Ada!"
                                        binding.btnDaftar.isEnabled = true
                                        binding.btnDaftar.text = "Daftar"
                                    } else {
                                        snapshot.ref.child("username").setValue(name)
                                        snapshot.ref.child("password").setValue(pwd)
                                        snapshot.ref.child("url_image").setValue(newImage[0])
                                        snapshot.ref.child("active").setValue(false)
                                        binding.btnDaftar.isEnabled = true
                                        binding.btnDaftar.text = "Daftar"

                                        sharedPreferences.edit().apply {
                                            putString(KEYLOGIN, keylogin_pedagang)
                                            putString(KEY_NAME, name)
                                            apply()
                                        }
                                        val intent =
                                            Intent(applicationContext, HomePedagangActivity::class.java)
                                        intent.putExtra(HomeActivity.KEY_NAME, name)
                                        startActivity(
                                            intent
                                        )
                                    }

                                override fun onCancelled(error: DatabaseError) {
                                    binding.btnDaftar.isEnabled = true
                                    binding.btnDaftar.text = "Daftar"
                                    Toast.makeText(
                                        applicationContext,
                                        error.message.toString(),
                                        Toast.LENGTH_LONG
                                    ).show()
                                }

                            })
                        }
                    }
                } else {
                    database.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                binding.nama.error = "Username Sudah Ada!"
                                binding.btnDaftar.isEnabled = true
                                binding.btnDaftar.text = "Daftar"
                            } else {
                                snapshot.ref.child("username").setValue(name)
                                snapshot.ref.child("password").setValue(pwd)
                                snapshot.ref.child("active").setValue(false)
                            //    snapshot.getRef().child("url_image")
                             //       .setValue("gs://hackathon-k5.appspot.com/icon_nopic.PNG")
                                binding.btnDaftar.isEnabled = true
                                binding.btnDaftar.text = "Daftar"

                                sharedPreferences.edit().apply {
                                    putString(KEYLOGIN, keylogin_pedagang)
                                    putString(KEY_NAME, name)
                                    apply()
                                }

                                val intent =
                                    Intent(applicationContext, HomePedagangActivity::class.java)
                                intent.putExtra(HomeActivity.KEY_NAME, name)
                                startActivity(
                                    intent
                                )
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            binding.btnDaftar.isEnabled = true
                            binding.btnDaftar.text = "Daftar"
                            Toast.makeText(
                                applicationContext,
                                error.message.toString(),
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    })
                }
            }
        }
    }

    private fun getFileExtension(uri: Uri?): String? {
        val contentResolver = contentResolver
        val mimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri!!))
    }

    private fun findImage() {
        val img = Intent()
        img.type = "image/*"
        img.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(img, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            uri = data.data!!
            Glide.with(applicationContext).load(uri).centerCrop().fitCenter().into(binding.ivPhoto)
        }
    }
}