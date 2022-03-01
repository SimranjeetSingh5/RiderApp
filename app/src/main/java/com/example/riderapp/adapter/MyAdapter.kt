package com.example.riderapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.riderapp.adapter.MyAdapter.MyViewHolder
import com.example.riderapp.databinding.ListItemBinding
import com.example.riderapp.models.Ride

class MyAdapter(private var rides:ArrayList<Ride>) : RecyclerView.Adapter<MyViewHolder>() {
    inner class MyViewHolder(val binding:ListItemBinding):RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        return MyViewHolder(
            ListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }


    override fun getItemCount(): Int {
        return rides.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.rides = rides[position]

        holder.binding.image.load(rides[position].map_url)
       }
}