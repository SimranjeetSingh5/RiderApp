package com.example.riderapp

import android.R
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Window
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.riderapp.adapter.MyAdapter
import com.example.riderapp.databinding.ActivityMainBinding
import com.example.riderapp.databinding.CustomDialogBinding
import com.example.riderapp.models.Ride
import com.example.riderapp.models.User
import com.example.riderapp.viewmodel.MyViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding
    private val viewModel:MyViewModel by viewModels()
    private lateinit var myAdapter: MyAdapter
    private lateinit var rides:ArrayList<Ride>
    private lateinit var upcoming:ArrayList<Ride>
    private lateinit var past:ArrayList<Ride>
    private lateinit var currentUserData: User
    private lateinit var state:ArrayList<String>
    private lateinit var city:ArrayList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        doInit()
        viewModel.responseUser.observe(this) {
            currentUserData = it
            binding.userName.text = it.name
            binding.userImage.load(it.url)
        }
        binding.filterBtn.setOnClickListener {
            showDialog()
        }
        binding.upcomingBtn.setOnClickListener {
            upcomingRV()
        }
        binding.pastBtn.setOnClickListener {
            pastRV()
        }
        binding.nearestBtn.setOnClickListener {
            setupRV()
        }
       setupRV()
    }

    private fun doInit() {
        rides = ArrayList()
        state = ArrayList()
        city = ArrayList()
        myAdapter = MyAdapter(rides)
        currentUserData = User("X",0,"")
    }

    private fun pastRV() {
        val c = Calendar.getInstance()
        val sdf = SimpleDateFormat("MM/dd/yyyy HH:mm aa")
        val getCurrentDateTime: String = sdf.format(c.time)
        Log.d("getCurrentDateTime", getCurrentDateTime)
        past = ArrayList()
        for (position in 0 until rides.size) {
            if (getCurrentDateTime.compareTo(rides[position].date) < 0) {
                past.add(rides[position])
            } else {
//                Toast.makeText(this,"Something went wrong",Toast.LENGTH_SHORT).show()
            }
        }
        myAdapter = MyAdapter(past)
        Log.d("CurrData",currentUserData.toString())
        binding.recyclerView.adapter = myAdapter
        myAdapter.notifyDataSetChanged()
    }

    private fun upcomingRV() {
        val c = Calendar.getInstance()
        val sdf = SimpleDateFormat("MM/dd/yyyy HH:mm aa")
        val getCurrentDateTime: String = sdf.format(c.time)
        Log.d("getCurrentDateTime", getCurrentDateTime)
        upcoming = ArrayList()
        for (position in 0 until rides.size) {
        if (getCurrentDateTime.compareTo(rides[position].date) > 0) {
            upcoming.add(rides[position])
        } else {
//            Toast.makeText(this,"Something went wrong",Toast.LENGTH_SHORT).show()
        }
        }
        myAdapter = MyAdapter(upcoming)
        Log.d("CurrData",currentUserData.toString())
        binding.recyclerView.adapter = myAdapter
        myAdapter.notifyDataSetChanged()
    }


    private fun setupRV() {

        viewModel.responseRide.observe(this) {
            rides.addAll(it)
            binding.nearestBtn.isChecked = true
            val myNumber = currentUserData.station_code
            for (position in 0 until rides.size) {
                var distance: Int = Math.abs(rides[position].station_path.get(0) - myNumber)
                var idx = 0
                for (c in 1 until rides[position].station_path.size) {
                    val cdistance: Int = Math.abs(rides[position].station_path.get(c) - myNumber)
                    if (cdistance < distance) {
                        idx = c
                        distance = cdistance
                    }
                }
                state.add(rides[position].state)
                city.add(rides[position].city)
                val theNumber: Int = rides[position].station_path.get(idx)
                rides[position].distance = Math.abs(currentUserData.station_code - theNumber)
            }
            rides.sortBy { it.distance }
            myAdapter = MyAdapter(rides)
            Log.d("CurrData", currentUserData.toString())

            binding.recyclerView.adapter = myAdapter
        }
        binding.recyclerView.apply {
            adapter = myAdapter
            layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
        }
    }
        private fun showDialog() {
            val dialog = Dialog(this)
            val dialogBinding = CustomDialogBinding.inflate(LayoutInflater.from(this))
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.window!!.attributes.gravity = Gravity.RIGHT
            dialog.setCancelable(true)
            dialog.setContentView(dialogBinding.root)


            val stateAdapter = ArrayAdapter(this,android.R.layout.simple_spinner_item,state)
            stateAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
            dialogBinding.stateSpin.setAdapter(stateAdapter)

            val cityAdapter = ArrayAdapter(this,android.R.layout.simple_spinner_item,city)
            cityAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
            dialogBinding.citySpin.setAdapter(cityAdapter)

            dialog.show()
        }



}