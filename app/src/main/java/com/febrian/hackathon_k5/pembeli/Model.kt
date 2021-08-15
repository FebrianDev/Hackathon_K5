package com.febrian.hackathon_k5.pembeli

import com.google.firebase.database.PropertyName
import java.math.BigInteger

data class Model(
    @get:PropertyName("nama_pemilik")
    @set:PropertyName("nama_pemilik")
    var namaPedagang : String? = null,
    @get:PropertyName("dagangan")
    @set:PropertyName("dagangan")
    var namaDagangan : String? = null,
    var description : String? = null,
    var active : Boolean? = false,
    @get:PropertyName("url_image")
    @set:PropertyName("url_image")
    var img : String? = null,
    var no : Double? = null,
    var long : Double? = null,
    var lat : Double? = null,
    var img0 : String? =null,
    var img1 : String? =null,
    var img2 : String? =null,
    var img3 : String? =null,
    var username : String? = null,
    var password : String? = null
)
