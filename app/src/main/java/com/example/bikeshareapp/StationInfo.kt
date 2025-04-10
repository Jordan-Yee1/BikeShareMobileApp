package com.example.bikeshareapp

data class StationInfoResponse(val data: StationInfoData)
data class StationInfoData(val stations: List<StationInfo>)
data class StationInfo(
    val station_id: String,
    val name: String,
    val lat: Double,
    val lon: Double
)
