package com.febrian.hackathon_k5.pembeli

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.database.PropertyName

data class Model(
    @get:PropertyName("nama_pemilik")
    @set:PropertyName("nama_pemilik")
    var namaPedagang : String? = null,
    @get:PropertyName("dagangan")
    @set:PropertyName("dagangan")
    var namaDagangan : String? = null,
    @get:PropertyName("deskripsi")
    @set:PropertyName("deskripsi")
    var description : String? = null,
    @get:PropertyName("url_image")
    @set:PropertyName("url_image")
    var img : String? = null,
    @get:PropertyName("wa")
    @set:PropertyName("wa")
    var no : Double? = null,

    val long : Double? = null,
    val lat : Double? = null,
    val active : Boolean? = false,

    val img0 : String? = null,
    val img1 : String? = null,
    val img2 : String? = null,
    val img3 : String? = null,

    var username : String? = null

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(namaPedagang)
        parcel.writeString(namaDagangan)
        parcel.writeString(description)
        parcel.writeString(img)
        parcel.writeValue(no)
        parcel.writeValue(long)
        parcel.writeValue(lat)
        parcel.writeValue(active)
        parcel.writeString(img0)
        parcel.writeString(img1)
        parcel.writeString(img2)
        parcel.writeString(img3)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Model> {
        override fun createFromParcel(parcel: Parcel): Model {
            return Model(parcel)
        }

        override fun newArray(size: Int): Array<Model?> {
            return arrayOfNulls(size)
        }
    }
}
