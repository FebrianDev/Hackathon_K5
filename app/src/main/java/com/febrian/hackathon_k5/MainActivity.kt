package com.febrian.hackathon_k5

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.febrian.hackathon_k5.pedagang.HomePedagangActivity
import com.febrian.hackathon_k5.pembeli.HomeActivity
import com.febrian.hackathon_k5.pembeli.HomePembeliActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    companion object{
        const val KEYLOGIN = "KEYLOGIN"
        const val keylogin_pedagang = "keylogin_pedagang"
        const val keylogin_pembeli = "keylogin_pembeli"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPref = applicationContext.getSharedPreferences(KEYLOGIN, Context.MODE_PRIVATE)
        val value = sharedPref.getString(KEYLOGIN, "")
        GlobalScope.launch {
            delay(800)

            if(value == keylogin_pedagang) {
                val intent = Intent(applicationContext, HomePedagangActivity::class.java)
                startActivity(intent)
                finish()
            }else if(value == keylogin_pembeli){
                val intent = Intent(applicationContext, HomePembeliActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                val intent = Intent(applicationContext, PedagangAtauPembeli::class.java)
                startActivity(intent)
                finish()
            }
        }

    }
}