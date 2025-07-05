package com.proyecto.appmascotassos4.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.location.Location
import android.location.LocationManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UbicacionViewModel(application: Application) : AndroidViewModel(application) {

    private val _ubicacion = MutableStateFlow(Pair(0.0, 0.0))
    val ubicacion = _ubicacion.asStateFlow()

    init {
        obtenerUbicacion()
    }

    @SuppressLint("MissingPermission")
    private fun obtenerUbicacion() {
        viewModelScope.launch {
            val locationManager = getApplication<Application>().getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val providers = locationManager.getProviders(true)

            for (provider in providers) {
                val location: Location? = locationManager.getLastKnownLocation(provider)
                if (location != null) {
                    _ubicacion.value = Pair(location.latitude, location.longitude)
                    break
                }
            }
        }
    }
}