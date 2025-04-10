package com.example.bikeshareapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CombinedStationAdapter(private val stationList: List<CombinedStation>) :
    RecyclerView.Adapter<CombinedStationAdapter.StationViewHolder>() {

    class StationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val stationName: TextView = itemView.findViewById(R.id.stationName)
        val bikesAvailable: TextView = itemView.findViewById(R.id.bikesAvailable)
        val docksAvailable: TextView = itemView.findViewById(R.id.docksAvailable)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.station_item, parent, false)
        return StationViewHolder(view)
    }

    override fun onBindViewHolder(holder: StationViewHolder, position: Int) {
        val station = stationList[position]
        holder.stationName.text = station.name
        holder.bikesAvailable.text = "Bikes: ${station.bikes}"
        holder.docksAvailable.text = "Docks: ${station.docks}"
    }

    //Get num station size
    override fun getItemCount(): Int = stationList.size




}
