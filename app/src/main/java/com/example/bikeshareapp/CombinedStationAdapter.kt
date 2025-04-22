package com.example.bikeshareapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CombinedStationAdapter(private val stationList: List<CombinedStation>) :
    RecyclerView.Adapter<CombinedStationAdapter.StationViewHolder>() {

    class StationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val stationName: TextView = itemView.findViewById(R.id.stationName)
        val bikesAvailable: TextView = itemView.findViewById(R.id.bikesAvailable)
        val docksAvailable: TextView = itemView.findViewById(R.id.docksAvailable)
        val favoriteButton: ImageButton = itemView.findViewById(R.id.favoriteButton)
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

        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val dbRef = FirebaseDatabase.getInstance().reference
        val stationKey = fixKey(station.name) //using name as ID
        val favoriteRef = dbRef.child("favorites").child(userId).child(stationKey)

        favoriteRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val isFavorited = snapshot.exists()
                holder.favoriteButton.setImageResource(
                    if (isFavorited) android.R.drawable.btn_star_big_on else android.R.drawable.btn_star_big_off
                )
            }

            override fun onCancelled(error: DatabaseError) {}
        })

        holder.favoriteButton.setOnClickListener {
            favoriteRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        favoriteRef.removeValue()
                        holder.favoriteButton.setImageResource(android.R.drawable.btn_star_big_off)
                    } else {
                        favoriteRef.setValue(true)
                        holder.favoriteButton.setImageResource(android.R.drawable.btn_star_big_on)
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        }
    }

    //Get num station size
    override fun getItemCount(): Int = stationList.size

    fun fixKey(input: String): String {
        return input
            .replace(".", ",")  // or "-"
            .replace("#", "")
            .replace("$", "")
            .replace("[", "")
            .replace("]", "")
            .replace("/", "-") // also a problem
    }




}
