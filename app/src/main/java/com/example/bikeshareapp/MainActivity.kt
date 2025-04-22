package com.example.bikeshareapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var stationRecyclerView: RecyclerView
    private lateinit var favoritesButton : Button
    companion object {
        var allStations: List<CombinedStation> = emptyList()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        stationRecyclerView = findViewById(R.id.stationRecyclerView)
        stationRecyclerView.layoutManager = LinearLayoutManager(this)
        favoritesButton = findViewById(R.id.favoriteScreenButton)

        favoritesButton.setOnClickListener{
            val intent = Intent(this, favorites::class.java)
            startActivity(intent)

        }
        lifecycleScope.launch {
            try {
                val infoResponse = ApiClient.api.getStationInfo()
                val statusResponse = ApiClient.api.getStationStatus()

                if (infoResponse.isSuccessful && statusResponse.isSuccessful) {
                    val stationInfos = infoResponse.body()?.data?.stations ?: emptyList()
                    val stationStatuses = statusResponse.body()?.data?.stations ?: emptyList()

                    val mergedStations = mutableListOf<CombinedStation>()
                    for (info in stationInfos){
                        val validID = stationStatuses.find { it.station_id == info.station_id }
                        if (validID != null){
                            val combined = CombinedStation(
                                name = info.name,
                                bikes = validID.num_bikes_available,
                                docks = validID.num_docks_available
                            )
                            mergedStations.add(combined)
                        }
                    }

                    allStations = mergedStations
                    stationRecyclerView.adapter = CombinedStationAdapter(mergedStations)

                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}