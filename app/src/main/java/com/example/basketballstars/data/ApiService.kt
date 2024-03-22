package com.example.basketballstars.data

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET

interface ApiService {
    @GET("/teams")
    suspend fun getTeams()
}