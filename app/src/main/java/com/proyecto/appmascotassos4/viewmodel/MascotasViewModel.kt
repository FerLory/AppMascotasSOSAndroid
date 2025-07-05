package com.proyecto.appmascotassos4.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.proyecto.appmascotassos4.model.MascotaPerdida
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.*

class MascotasViewModel : ViewModel() {

    private val _mascotasCercanas = MutableStateFlow<List<MascotaPerdida>>(emptyList())
    val mascotasCercanas = _mascotasCercanas.asStateFlow()

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private var latitudUsuario = 0.0
    private var longitudUsuario = 0.0

    fun setUbicacionUsuario(lat: Double, lon: Double) {
        latitudUsuario = lat
        longitudUsuario = lon
        cargarMascotas()
    }

    private fun cargarMascotas() {
        viewModelScope.launch {
            val lista = mutableListOf<MascotaPerdida>()

            val uid = auth.currentUser?.uid ?: return@launch
            firestore.collectionGroup("mascotas_perdidas")
                .get()
                .addOnSuccessListener { result ->
                    for (doc in result) {
                        val mascota = doc.toObject(MascotaPerdida::class.java)
                        val distancia = distanciaEnKm(
                            latitudUsuario,
                            longitudUsuario,
                            mascota.latitud,
                            mascota.longitud
                        )
                        if (distancia <= 5.0) {
                            lista.add(mascota)
                        }
                    }
                    _mascotasCercanas.value = lista
                }
        }
    }

    private fun distanciaEnKm(
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double
    ): Double {
        val r = 6371
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2).pow(2.0) + cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) * sin(dLon / 2).pow(2.0)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return r * c
    }
}