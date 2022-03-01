package com.example.riderapp.api

import com.example.riderapp.models.Ride
import com.example.riderapp.models.User
import com.example.riderapp.utils.Constants
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET(Constants.USER)
    suspend fun getUser():Response<User>

    @GET(Constants.RIDES)
    suspend fun getRides():Response<List<Ride>>
}