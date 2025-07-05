package com.proyecto.appmascotassos4.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.proyecto.appmascotassos4.R

data class DatosDueño(
    val nombre: String = "",
    val correo: String = "",
    val telefono: String = "",
    val direccion: String = ""
)

class PerfilViewModel(application: Application) : AndroidViewModel(application) {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val context = getApplication<Application>().applicationContext

    private val _datosDueño = MutableStateFlow<DatosDueño?>(null)
    val datosDueño: StateFlow<DatosDueño?> = _datosDueño

    init {
        cargarDatosDueño()
    }

    private fun cargarDatosDueño() {
        val uid = auth.currentUser?.uid ?: return
        db.collection(context.getString(R.string.coleccion_duenos)).document(uid).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val datos = DatosDueño(
                        nombre = document.getString(context.getString(R.string.campo_nombre)) ?: "",
                        correo = document.getString(context.getString(R.string.campo_correo)) ?: "",
                        telefono = document.getString(context.getString(R.string.campo_telefono)) ?: "",
                        direccion = document.getString(context.getString(R.string.campo_direccion)) ?: ""
                    )
                    _datosDueño.value = datos
                }
            }
    }
}