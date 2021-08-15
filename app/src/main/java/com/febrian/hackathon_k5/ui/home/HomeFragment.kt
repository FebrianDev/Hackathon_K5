package com.febrian.hackathon_k5.ui.home

import android.content.*
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.febrian.hackathon_k5.MainActivity
import com.febrian.hackathon_k5.R
import com.febrian.hackathon_k5.databinding.FragmentHomeBinding
import com.febrian.hackathon_k5.pedagang.HomeActivity
import com.febrian.hackathon_k5.pembeli.AdapterHome
import com.febrian.hackathon_k5.pembeli.LoginPembeli2Activity
import com.febrian.hackathon_k5.pembeli.LoginPembeli2Activity.Companion.KEY_NAME
import com.febrian.hackathon_k5.pembeli.Model
import com.google.firebase.database.*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var database: DatabaseReference

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var sharedPreferences : SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        sharedPreferences = view.context.getSharedPreferences(
            MainActivity.KEYLOGIN,
            AppCompatActivity.MODE_PRIVATE
        )

        binding.search.queryHint =
            Html.fromHtml("<font color = #067451>" + "Cari Disini" + "</font>")

        val name = sharedPreferences.getString(KEY_NAME, "")
        val list = ArrayList<Model>()
        database = FirebaseDatabase.getInstance().getReference("UsersPedagang")
        database.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                for (dataSnapshot in snapshot.children) {
                    val d: Model = dataSnapshot.getValue(Model::class.java)!!
                    list.add(d)
                }


                binding.rv.layoutManager = GridLayoutManager(view.context, 2)
                binding.rv.setHasFixedSize(true)
                binding.rv.adapter = AdapterHome(list)

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(view.context, error.message, Toast.LENGTH_LONG).show()
            }

        })

        val databasePembeli = FirebaseDatabase.getInstance().getReference("UsersPembeli").child(name.toString())
        databasePembeli.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.name.setText(snapshot.child("username").value.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(view.context, error.message, Toast.LENGTH_LONG).show()
            }

        })
    }

    var i = 0

    private var broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            i++
            Log.d("Inc", i.toString())

        }
    }

    override fun onStart() {
        super.onStart()
        val intent = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        view?.context?.registerReceiver(broadcastReceiver, intent)
    }

    override fun onStop() {
        super.onStop()
        view?.context?.unregisterReceiver(broadcastReceiver)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}