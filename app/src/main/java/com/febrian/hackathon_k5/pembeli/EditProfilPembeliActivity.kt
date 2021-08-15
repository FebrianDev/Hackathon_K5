package com.febrian.hackathon_k5.pembeli

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.MimeTypeMap
import android.widget.Toast
import com.bumptech.glide.Glide
import com.febrian.hackathon_k5.MainActivity
import com.febrian.hackathon_k5.databinding.ActivityEditProfilPembeliBinding
import com.febrian.hackathon_k5.pedagang.Register2Activity
import com.febrian.hackathon_k5.pembeli.LoginPembeli2Activity.Companion.KEY_NAME
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage

class EditProfilPembeliActivity : AppCompatActivity() {

    private lateinit var binding : ActivityEditProfilPembeliBinding
    private lateinit var database: DatabaseReference

    private lateinit var sharedPreferences : SharedPreferences

    private var uri: Uri? = null

    val PICK_IMAGE_REQUEST = 1000


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfilPembeliBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences(MainActivity.KEYLOGIN, MODE_PRIVATE)

        binding.addPhoto.setOnClickListener {
            findImage()
        }

        binding.back.setOnClickListener {
            finish()
        }

        val name = sharedPreferences.getString(KEY_NAME, "")
        database =  FirebaseDatabase.getInstance().reference.child("UsersPembeli").child(name.toString())

        database.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.nama.setText(snapshot.child("username").value.toString())
                binding.password.setText(snapshot.child("password").value.toString())

                if(snapshot.child("url_image").value != null)
                    Glide.with(applicationContext).load(snapshot.child("url_image").value.toString()).into(binding.ivPhoto)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, error.message, Toast.LENGTH_LONG).show()
            }

        })

        binding.btnEdit.setOnClickListener {

            val name = binding.nama.text.toString()
            val pwd = binding.password.text.toString()

            val databaseEdit = FirebaseDatabase.getInstance().reference.child("UsersPembeli").child(name)
            val storage = FirebaseStorage.getInstance().reference.child("ImageUsers")
            val newImage = arrayOfNulls<String>(1)

            if (name.isEmpty()) {
                binding.nama.error = "Username Harus Diisi!"
            } else if (pwd.isEmpty()) {
                binding.password.error = "Password Harus Diisi!"
            } else if (pwd.length < 6) {
                binding.password.error = "Password Harus Minimal 6"
            } else {
                binding.btnEdit.isEnabled = false
                binding.btnEdit.text = "Loading...."
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
                                    if (snapshot.child("username").value.toString() == name) {
                                        binding.nama.error = "Username Sudah Ada!"
                                        binding.btnEdit.isEnabled = true
                                        binding.btnEdit.text = "Daftar"
                                    } else {
                                        snapshot.ref.child("username").setValue(name)
                                        snapshot.ref.child("password").setValue(pwd)
                                        snapshot.ref.child("url_image").setValue(newImage[0])
                                        binding.btnEdit.isEnabled = true
                                        binding.btnEdit.text = "Daftar"

                                        finish()
                                    }

                                override fun onCancelled(error: DatabaseError) {
                                    binding.btnEdit.isEnabled = true
                                    binding.btnEdit.text = "Daftar"
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
                else{
                    database.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                binding.nama.error = "Username Sudah Ada!"
                                binding.btnEdit.isEnabled = true
                                binding.btnEdit.text = "Edit"
                            } else {
                                snapshot.ref.child("username").setValue(name)
                                snapshot.ref.child("password").setValue(pwd)
                                //   snapshot.getRef().child("url_image").setValue("gs://hackathon-k5.appspot.com/icon_nopic.PNG")
                                binding.btnEdit.isEnabled = true
                                binding.btnEdit.text = "Edit"

                                finish()

                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            binding.btnEdit.isEnabled = true
                            binding.btnEdit.text = "Edit"
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
        startActivityForResult(img, Register2Activity.PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Register2Activity.PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            uri = data.data!!
            Glide.with(applicationContext).load(uri).centerCrop().fitCenter().into(binding.ivPhoto)
        }
    }

}