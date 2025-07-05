package com.proyecto.appmascotassos4.viewmodel

import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MapaViewModel : ViewModel() {

    private val _ubicacion = MutableStateFlow<LatLng?>(null)
    val ubicacion: StateFlow<LatLng?> = _ubicacion

    private val _direccion = MutableStateFlow("")
    val direccion: StateFlow<String> = _direccion

    fun setUbicacion(latLng: LatLng, dir: String) {
        _ubicacion.value = latLng
        _direccion.value = dir
    }
}