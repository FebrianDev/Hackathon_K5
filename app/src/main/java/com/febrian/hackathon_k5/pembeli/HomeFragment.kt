package com.febrian.hackathon_k5.pembeli

import android.content.*
import android.net.ConnectivityManager
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.febrian.hackathon_k5.MainActivity
import com.febrian.hackathon_k5.databinding.FragmentHomeBinding
import com.febrian.hackathon_k5.pembeli.LoginPembeli2Activity.Companion.KEY_NAME
import com.google.firebase.database.*

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var database: DatabaseReference

    // This property is only valid between onCreateView and
    // onDestroyView.
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loading.visibility = View.VISIBLE

        sharedPreferences = view.context.getSharedPreferences(
            MainActivity.KEYLOGIN,
            AppCompatActivity.MODE_PRIVATE
        )

        binding.search.queryHint =
            Html.fromHtml("<font color = #067451>" + "Cari Disini" + "</font>")

        val name = sharedPreferences.getString(KEY_NAME, "")
        submit("")

        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                submit(query.toString())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                submit(newText.toString())
                return true
            }

        })


        val databasePembeli =
            FirebaseDatabase.getInstance().getReference("UsersPembeli").child(name.toString())
        databasePembeli.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.name.setText(snapshot.child("username").value.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(view.context, error.message, Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun submit(s: String) {
        binding.loading.visibility = View.VISIBLE

        val list = ArrayList<Model>()
        database = FirebaseDatabase.getInstance().getReference("UsersPedagang")
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.loading.visibility = View.GONE
                for (dataSnapshot in snapshot.children) {
                    val d: Model = dataSnapshot.getValue(Model::class.java)!!
                    if (d.active as Boolean)
                        list.add(d)
                }

                if (s == "" || s.isEmpty()) {
                    binding.rv.layoutManager = GridLayoutManager(view!!.context, 2)
                    binding.rv.setHasFixedSize(true)
                    binding.rv.adapter = AdapterHome(list)
                } else {
                    val l = ArrayList<Model>()

                    for (i in 0..list.size-1) {
                        if(list[i].namaDagangan == s){
                            l.add(list[i])
                        }
                    }
                        binding.rv.layoutManager = GridLayoutManager(view!!.context, 2)
                        binding.rv.setHasFixedSize(true)
                        binding.rv.adapter = AdapterHome(l)

                }
            }

            override fun onCancelled(error: DatabaseError) {
                binding.loading.visibility = View.GONE
                Toast.makeText(view!!.context, error.message, Toast.LENGTH_LONG).show()
            }

        })
    }

    private var broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {

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

}