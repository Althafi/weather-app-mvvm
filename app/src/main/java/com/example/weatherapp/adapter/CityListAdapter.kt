package com.example.weatherapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.activity.MainActivity
import com.example.weatherapp.databinding.CityViewholderBinding
import com.example.weatherapp.databinding.ForecastViewholderBinding
import com.example.weatherapp.model.CityListResponseApi

class CityListAdapter : RecyclerView.Adapter<CityListAdapter.ViewHolder>(){
    private lateinit var binding: CityViewholderBinding
    inner class ViewHolder : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): CityListAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = CityViewholderBinding.inflate(inflater, parent, false)
        return ViewHolder()
    }

    override fun onBindViewHolder(holder: CityListAdapter.ViewHolder, position: Int) {
        val binding = CityViewholderBinding.bind(holder.itemView)
        binding.tvCity.text = differ.currentList[position].name
        binding.root.setOnClickListener {
            val intent = Intent(binding.root.context, MainActivity::class.java)
            intent.putExtra("lat", differ.currentList[position].lat)
            intent.putExtra("lon", differ.currentList[position].lon)
            intent.putExtra("name", differ.currentList[position].name)
            binding.root.context.startActivity(intent)
        }
    }


    override fun getItemCount() = differ.currentList.size

    private val differCallback = object : DiffUtil.ItemCallback<CityListResponseApi.CityListItem>() {
        override fun areItemsTheSame(
            oldItem: CityListResponseApi.CityListItem,
            newItem: CityListResponseApi.CityListItem,
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: CityListResponseApi.CityListItem,
            newItem: CityListResponseApi.CityListItem,
        ): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, differCallback)
}