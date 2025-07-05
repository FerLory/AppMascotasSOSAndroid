package com.proyecto.appmascotassos4.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.proyecto.appmascotassos4.model.Mascota
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MascotasGlobalViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _mascotas = MutableStateFlow<List<Mascota>>(emptyList())
    val mascotas = _mascotas.asStateFlow()

    private val _mensaje = MutableStateFlow("")
    val mensaje = _mensaje.asStateFlow()

    fun obtenerMascotasGlobal() {
        db.collectionGroup("mascotas_perdidas")
            .get()
            .addOnSuccessListener { resultado ->
                val lista = resultado.mapNotNull { it.toObject(Mascota::class.java) }
                _mascotas.value = lista
                _mensaje.value = "Mascotas cargadas"
            }
            .addOnFailureListener { e ->
                _mensaje.value = "Error al obtener mascotas: ${e.message}"
            }
    }
}