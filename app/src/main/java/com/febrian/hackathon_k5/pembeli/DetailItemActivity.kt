package com.febrian.hackathon_k5.pembeli

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.febrian.hackathon_k5.databinding.ActivityDetailItemBinding
import com.febrian.hackathon_k5.pembeli.LoginPembeli2Activity.Companion.KEY_NAME
import com.google.firebase.database.*

class DetailItemActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDetailItemBinding
    private lateinit var database: DatabaseReference
companion object{
    const val KEY_DETAIL = "KEY_DETAIL"
}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra(KEY_DETAIL)

        binding.back.setOnClickListener {
            val intent = Intent(applicationContext, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
        val imageList = ArrayList<SlideModel>()
        database = FirebaseDatabase.getInstance().getReference("UsersPedagang").child(name.toString())
        database.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(i in 0..3){
                    if(snapshot.child("img$i").value.toString() != null) {
                        imageList.add(SlideModel(snapshot.child("img$i").value.toString()))
                    }
                }
                val imageSlider = binding.img
                imageSlider.setImageList(imageList)

                binding.dagangan.text = snapshot.child("dagangan").value.toString()
                binding.pemilik.text = snapshot.child("nama_pemilik").value.toString()
                binding.deskripsi.text = snapshot.child("deskripsi").value.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, error.message,Toast.LENGTH_LONG).show()
            }

        })

    }

}