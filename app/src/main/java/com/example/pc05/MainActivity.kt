package com.example.pc05

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.example.pc05.data.WeatherRepository
import com.example.pc05.data.local.WeatherDatabase
import com.example.pc05.data.remote.RetrofitInstance
import com.example.pc05.ui.WeatherScreen
import com.example.pc05.ui.WeatherViewModel
import com.example.pc05.ui.theme.PC05Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val database = WeatherDatabase.getDatabase(this)
        val repository = WeatherRepository(RetrofitInstance.api, database.weatherLogDao())
        val viewModel = ViewModelProvider(this, WeatherViewModel.Factory(repository))[WeatherViewModel::class.java]

        enableEdgeToEdge()
        setContent {
            PC05Theme {
                WeatherScreen(viewModel)
            }
        }
    }
}
