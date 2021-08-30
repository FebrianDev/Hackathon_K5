package com.febrian.hackathon_k5.pedagang

import android.R.attr
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Toast
import com.bumptech.glide.Glide
import com.febrian.hackathon_k5.databinding.ActivityAddDaganganBinding
import com.febrian.hackathon_k5.pedagang.HomePedagangFragment.Companion.KEY_NAME
import com.febrian.hackathon_k5.pembeli.Model
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import androidx.core.app.ActivityCompat.startActivityForResult
import android.R.attr.data
import android.annotation.SuppressLint
import android.app.Activity
import androidx.annotation.Nullable
import com.google.android.gms.tasks.OnSuccessListener

import com.google.firebase.storage.UploadTask
import androidx.annotation.NonNull
import androidx.recyclerview.widget.LinearLayoutManager

import com.google.android.gms.tasks.OnCompleteListener


class AddDagangan : AppCompatActivity() {

    private lateinit var binding: ActivityAddDaganganBinding
    private lateinit var database: DatabaseReference
    private lateinit var storage: StorageReference
    private var uri: Uri? = null
    private var uploads = 0

    companion object {
        const val PICK_IMAGE_REQUEST1 = 1
        const val PICK_IMG = 111
        const val PICK_IMAGE_REQUEST2 = 2
        const val PICK_IMAGE_REQUEST3 = 3
        const val PICK_IMAGE_REQUEST4 = 4
        const val KEYLOGIN = "KEYLOGIN"
        const val keylogin = "keylogin"
    }

    private val ImageList = ArrayList<Uri>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddDaganganBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loading.visibility = View.VISIBLE

        val name = intent.getStringExtra(KEY_NAME)
        database = FirebaseDatabase.getInstance().getReference("UsersPedagang").child(name.toString())
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.child("nama_pemilik").value != null)
                    binding.nama.setText(snapshot.child("nama_pemilik").value.toString())
                if (snapshot.child("dagangan").value != null)
                    binding.dagangan.setText(snapshot.child("dagangan").value.toString())
                if (snapshot.child("wa").value != null)
                    binding.wa.setText(snapshot.child("wa").value.toString())
                if (snapshot.child("deskripsi").value != null)
                    binding.deskripsi.setText(snapshot.child("deskripsi").value.toString())

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
                        applicationContext, LinearLayoutManager.HORIZONTAL,
                        false
                    )
                    binding.rv.adapter = AdapterHomeString(list)
                } else {
                    binding.linearLayout2.visibility = View.VISIBLE
                    binding.rv.visibility = View.GONE
                }

                binding.loading.visibility = View.GONE
            }

            override fun onCancelled(error: DatabaseError) {
                binding.loading.visibility = View.GONE
                Toast.makeText(applicationContext, error.message.toString(), Toast.LENGTH_LONG)
                    .show()
            }

        })

        storage = FirebaseStorage.getInstance().getReference("Users").child("ImagePedagang")

        binding.simpan.setOnClickListener {
            binding.loading.visibility = View.VISIBLE
            if (binding.nama.text.toString() == "") {
                binding.nama.error = "Nama tidak boleh kosong!"
            } else if (binding.dagangan.text.toString() == "") {
                binding.dagangan.error = "Nama Dagangan tidak boleh kosong!"
            } else if (binding.wa.text.toString() == "") {
                binding.wa.error = "No WhatsApp tidak boleh kosong!"
            } else if (binding.wa.text.toString().length < 10 || binding.wa.text.toString().length > 12) {
                binding.wa.error = "No WhatsApp tidak valid!"

            } else if (binding.deskripsi.text.toString() == "") {
                binding.deskripsi.error = "Deskripsi tidak boleh kosong!"
            } else {
                binding.simpan.isEnabled = false
                binding.simpan.text = "Loading..."
                upload(getData())
            }

        }

        binding.addImgDagangan.setOnClickListener {
            choose()
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun getData(): Model {
        val m = Model(
        namaPedagang = binding.nama.text.toString(),
        namaDagangan = binding.dagangan.text.toString(),
        no = binding.wa.text.toString().toDouble(),
        active = false,
        description = binding.deskripsi.text.toString()
        )
            return m
    }

    private fun upload(m: Model) {
        val ImageFolder = FirebaseStorage.getInstance().reference.child("ImageFolder")
        uploads = 0
        if(ImageList.size != 0) {
            while (uploads < ImageList.size) {
                val Image = ImageList[uploads]
                val imagename = ImageFolder.child("image/" + Image.lastPathSegment)
                imagename.putFile(ImageList[uploads]).addOnSuccessListener {
                    imagename.downloadUrl.addOnSuccessListener { uri ->
                        val url = uri.toString()
                        SendLink(url, uploads, m)
                    }
                }
                uploads++
            }
        }else{
            database.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    binding.loading.visibility = View.GONE
                    snapshot.ref.child("nama_pemilik").setValue(m.namaPedagang)
                    snapshot.ref.child("dagangan").setValue(m.namaDagangan)
                    snapshot.ref.child("deskripsi").setValue(m.description)
                    snapshot.ref.child("wa").setValue(m.no)

                    binding.simpan.isEnabled = true
                    binding.simpan.text = "Simpan"
                    ImageList.clear()
                   finish()
                }

                override fun onCancelled(error: DatabaseError) {
                    binding.loading.visibility = View.GONE
                    binding.simpan.isEnabled = true
                    binding.simpan.text = "Simpan"
                    Toast.makeText(applicationContext, error.message.toString(), Toast.LENGTH_LONG)
                        .show()
                }

            })
        }
    }

    val listImg = ArrayList<String>()
        private fun SendLink(url: String, i: Int, m: Model) {
        listImg.add(url)
        if (listImg.size == ImageList.size) {
            //Toast.makeText(applicationContext, "$i ${ImageList.size}", Toast.LENGTH_LONG).show()
            database.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.ref.child("nama_pemilik").setValue(m.namaPedagang)
                    snapshot.ref.child("dagangan").setValue(m.namaDagangan)
                    snapshot.ref.child("deskripsi").setValue(m.description)
                    snapshot.ref.child("active").setValue(m.active)
                    snapshot.ref.child("wa").setValue(m.no)
                    for(i in 0 until listImg.size)
                        snapshot.ref.child("img$i").setValue(listImg[i])

                    binding.simpan.isEnabled = true
                    binding.simpan.text = "Simpan"
                    ImageList.clear()
                    finish()
                    binding.loading.visibility = View.GONE
                }

                override fun onCancelled(error: DatabaseError) {
                    binding.simpan.isEnabled = true
                    binding.loading.visibility = View.GONE
                    binding.simpan.text = "Simpan"
                    Toast.makeText(applicationContext, error.message.toString(), Toast.LENGTH_LONG)
                        .show()
                }

            })
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMG) {
            if (resultCode == RESULT_OK) {
                if (data?.clipData != null) {
                    val count = data.clipData!!.itemCount
                    var currentImageSelect: Int = 0

                    if (count > 4) {
                        while (currentImageSelect < 4) {
                            val imageuri = data.clipData!!.getItemAt(currentImageSelect).uri
                            ImageList.add(imageuri)
                            currentImageSelect = currentImageSelect + 1
                        }
                    } else {
                        while (currentImageSelect < count) {
                            val imageuri = data.clipData!!.getItemAt(currentImageSelect).uri
                            ImageList.add(imageuri)
                            currentImageSelect = currentImageSelect + 1
                        }
                    }

                    binding.rv.visibility = View.VISIBLE
                    binding.linearLayout2.visibility = View.GONE
                    binding.rv.layoutManager = LinearLayoutManager(
                        applicationContext, LinearLayoutManager.HORIZONTAL,
                        false
                    )
                    binding.rv.adapter = AdapterImage(ImageList)
                }
            }
        }
    }

    private fun choose() {
        //we will pick images
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(intent, PICK_IMG)
    }
}