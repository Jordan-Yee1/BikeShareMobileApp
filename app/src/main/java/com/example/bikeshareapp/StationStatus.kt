package com.example.bikeshareapp

data class StationStatusResponse(val data: StationStatusData)
data class StationStatusData(val stations: List<StationStatus>)
data class StationStatus(
    val station_id: String,
    val num_bikes_available: Int,
    val num_docks_available: Int
)
