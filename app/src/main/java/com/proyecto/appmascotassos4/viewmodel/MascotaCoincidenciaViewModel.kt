package com.proyecto.appmascotassos4.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.proyecto.appmascotassos4.model.MascotaEncontrada
import com.proyecto.appmascotassos4.model.MascotaPerdida
import com.proyecto.appmascotassos4.utils.mostrarNotificacion
import com.proyecto.appmascotassos4.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.math.*

class MascotaCoincidenciaViewModel(application: Application) : AndroidViewModel(application) {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _coincidencias = MutableStateFlow<List<MascotaEncontrada>>(emptyList())
    val coincidencias: StateFlow<List<MascotaEncontrada>> = _coincidencias

    fun verificarCoincidencias(mascotaPerdida: MascotaPerdida) {
        viewModelScope.launch {
            db.collectionGroup("mascotas_encontradas").get()
                .addOnSuccessListener { documentos ->
                    val lista = documentos.mapNotNull { it.toObject(MascotaEncontrada::class.java) }
                    val coincidenciasFiltradas = lista.filter { encontrada ->
                        encontrada.raza.equals(mascotaPerdida.raza, ignoreCase = true) &&
                                calcularDistancia(
                                    mascotaPerdida.latitud,
                                    mascotaPerdida.longitud,
                                    encontrada.latitud,
                                    encontrada.longitud
                                ) <= 5
                    }
                    _coincidencias.value = coincidenciasFiltradas

                    if (coincidenciasFiltradas.isNotEmpty()) {
                        val titulo = getApplication<Application>().getString(R.string.notificacion_titulo)
                        val mensaje = getApplication<Application>().getString(R.string.notificacion_mensaje)
                        mostrarNotificacion(getApplication(), titulo, mensaje)
                    }
                }
        }
    }

    private fun calcularDistancia(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val radio = 6371.0
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2).pow(2.0) + cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) * sin(dLon / 2).pow(2.0)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return radio * c
    }
}