package com.example.bikeshareapp

import android.os.Bundle
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

        lifecycleScope.launch {
            try {
                val infoResponse = ApiClient.api.getStationInfo()
                val statusResponse = ApiClient.api.getStationStatus()

                if (infoResponse.isSuccessful && statusResponse.isSuccessful) {
                    val stationInfos = infoResponse.body()?.data?.stations ?: emptyList()
                    val stationStatuses = statusResponse.body()?.data?.stations ?: emptyList()

                    val mergedStations = stationInfos.mapNotNull { info ->
                        val status = stationStatuses.find { it.station_id == info.station_id }
                        status?.let {
                            CombinedStation(
                                name = info.name,
                                bikes = it.num_bikes_available,
                                docks = it.num_docks_available
                            )
                        }
                    }

                    stationRecyclerView.adapter = CombinedStationAdapter(mergedStations)

                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}