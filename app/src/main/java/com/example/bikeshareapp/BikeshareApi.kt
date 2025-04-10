package com.example.bikeshareapp

import retrofit2.Response
import retrofit2.http.GET

interface BikeshareApi {
    @GET("gbfs/en/station_information.json")
    suspend fun getStationInfo(): Response<StationInfoResponse>

    @GET("gbfs/en/station_status.json")
    suspend fun getStationStatus(): Response<StationStatusResponse>
}
