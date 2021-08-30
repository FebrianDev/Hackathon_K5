package com.febrian.hackathon_k5.pembeli

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.febrian.hackathon_k5.R
import com.febrian.hackathon_k5.databinding.ItemHomeBinding

class AdapterHome(private val list : ArrayList<Model>) : RecyclerView.Adapter<AdapterHome.ViewHolder>() {

    companion object{
        const val KEY_DATA = "KEY_DATA"
    }

    inner class ViewHolder(private val binding : ItemHomeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(m : Model){
            with(binding){
                binding.namaDagangan.text = m.namaDagangan.toString()
                binding.namaPedagang.text = m.namaPedagang.toString()
                Glide.with(itemView).load(m.img0).into(binding.img)

                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, DetailItemActivity::class.java)
                    intent.putExtra(DetailItemActivity.KEY_DETAIL, m.username)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterHome.ViewHolder {
        val view = ItemHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdapterHome.ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}