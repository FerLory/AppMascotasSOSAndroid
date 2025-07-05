package com.proyecto.appmascotassos4.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.ktx.firestore

class DuenoViewModel : ViewModel() {
    private val db = Firebase.firestore
    fun guardarDatosDueño(
        nombre: String,
        telefono: String,
        direccion: String,
        latitud: Double,
        longitud: Double
    ) {
        val data = hashMapOf(
            "nombre" to nombre,
            "telefono" to telefono,
            "direccion" to direccion,
            "latitud" to latitud,
            "longitud" to longitud
        )
        db.collection("duenos").add(data)
    }
}