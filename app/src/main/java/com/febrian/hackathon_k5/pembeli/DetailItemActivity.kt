package com.febrian.hackathon_k5.pembeli

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.febrian.hackathon_k5.HomeActivity
import com.febrian.hackathon_k5.databinding.ActivityDetailItemBinding

class DetailItemActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDetailItemBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener {
            val intent = Intent(applicationContext, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel("https://i.stack.imgur.com/nGMDF.png", "The animal population decreased by 58 percent in 42 years.", ScaleTypes.CENTER_INSIDE))
        imageList.add(SlideModel("https://www.instagram.com/p/B98TxlrAa8H/", "Elephants and tigers may become extinct.",ScaleTypes.CENTER_CROP))
        imageList.add(SlideModel("https://www.instagram.com/p/B3EJdu0hFNH/", "And people do that.",ScaleTypes.FIT))

        val imageSlider = binding.img
        imageSlider.setImageList(imageList)
    }

}