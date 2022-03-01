package com.example.riderapp.repository

import com.example.riderapp.api.ApiService
import javax.inject.Inject

class MyRepository
@Inject
constructor(private val apiService: ApiService){

    suspend fun getUser() = apiService.getUser()

    suspend fun getRides() = apiService.getRides()
}