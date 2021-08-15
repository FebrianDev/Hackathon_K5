package com.febrian.hackathon_k5.pedagang

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.febrian.hackathon_k5.databinding.ItemImgBinding

class AdapterHomeString (listData : ArrayList<String>) : RecyclerView.Adapter<AdapterHomeString.ViewHolder>(){
    private val list = listData

    class ViewHolder(private val binding : ItemImgBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(s : String){
            Glide.with(itemView).load(s).centerCrop().fitCenter().into(binding.itemImg)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterHomeString.ViewHolder {
        val view = ItemImgBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdapterHomeString.ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return if(list.size > 4) 4
        else list.size
    }
}