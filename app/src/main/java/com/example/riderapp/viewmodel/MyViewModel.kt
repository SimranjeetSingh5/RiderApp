package com.example.riderapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.riderapp.models.Ride
import com.example.riderapp.models.User
import com.example.riderapp.repository.MyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyViewModel
@Inject
constructor(private val repository: MyRepository):ViewModel(){

    private val _userResponse = MutableLiveData<User>()
    val responseUser:LiveData<User>
    get() = _userResponse

    private val _ridesResponse = MutableLiveData<List<Ride>>()
    val responseRide: MutableLiveData<List<Ride>>
    get() = _ridesResponse

    init {
        getUserDetails()
        getRides()
    }

    private fun getRides() = viewModelScope.launch {
        repository.getRides().let { response ->
            if (response.isSuccessful){
                _ridesResponse.postValue(response.body())
            }else{
                Log.d("tag","getRidesError = ${response.code()}")
            }
        }
    }

    private fun getUserDetails() = viewModelScope.launch {
        repository.getUser().let { response ->
            if (response.isSuccessful){
                _userResponse.postValue(response.body())
            } else {
                Log.d("tag","getUserDetailsError  = ${response.code()}")
            }
        }
    }
}