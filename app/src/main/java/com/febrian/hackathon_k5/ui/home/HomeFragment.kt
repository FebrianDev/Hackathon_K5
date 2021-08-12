package com.febrian.hackathon_k5.ui.home

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
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
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.febrian.hackathon_k5.R
import com.febrian.hackathon_k5.databinding.FragmentHomeBinding
import com.febrian.hackathon_k5.pembeli.AdapterHome
import com.febrian.hackathon_k5.pembeli.Model

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.search.queryHint = Html.fromHtml("<font color = #067451>"  +"Cari Disini"+ "</font>")

        val list = ArrayList<Model>()

        for(i in 0..10) {
            list.add(Model(namaDagangan = "Nasi Goreng", namaPedagang = "Pak Joni"))
        }

        binding.rv.layoutManager = GridLayoutManager(view.context, 2)
        binding.rv.setHasFixedSize(true)
        binding.rv.adapter = AdapterHome(list)

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