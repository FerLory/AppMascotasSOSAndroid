package com.proyecto.appmascotassos4.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.proyecto.appmascotassos4.model.MascotaEncontrada
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.proyecto.appmascotassos4.R

class MascotaEncontradaViewModel(application: Application) : AndroidViewModel(application) {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val context = getApplication<Application>().applicationContext

    private val _lista = MutableStateFlow<List<MascotaEncontrada>>(emptyList())
    val lista = _lista.asStateFlow()

    private var lastVisible: DocumentSnapshot? = null
    private var isLoading = false
    private val pageSize = 10

    init {
        cargarMas()
    }

    fun cargarMas() {
        if (isLoading) return
        isLoading = true
        viewModelScope.launch {
            val base = db.collection("mascotas_encontradas")
                .orderBy(context.getString(R.string.campo_fecha_hora))
                .limit(pageSize.toLong())
            val query = if (lastVisible != null) base.startAfter(lastVisible!!) else base
            query.get()
                .addOnSuccessListener { snap ->
                    if (!snap.isEmpty) {
                        lastVisible = snap.documents.last()
                        val nuevas = snap.documents.map { d ->
                            MascotaEncontrada(
                                nombre = d.getString(context.getString(R.string.campo_nombre)) ?: "",
                                especie = d.getString(context.getString(R.string.campo_especie)) ?: "",
                                raza = d.getString(context.getString(R.string.campo_raza)) ?: "",
                                senas = d.getString(context.getString(R.string.campo_senas)) ?: "",
                                fechaHora = d.getString(context.getString(R.string.campo_fecha_hora)) ?: "",
                                fotoUrl = d.getString(context.getString(R.string.campo_foto_url)) ?: "",
                                colonia = d.getString(context.getString(R.string.campo_colonia)) ?: "",
                                direccion = d.getString(context.getString(R.string.campo_direccion)) ?: "",
                                latitud = d.getDouble(context.getString(R.string.campo_latitud)) ?: 0.0,
                                longitud = d.getDouble(context.getString(R.string.campo_longitud)) ?: 0.0
                            )
                        }
                        _lista.value = _lista.value + nuevas
                    }
                    isLoading = false
                }
                .addOnFailureListener { isLoading = false }
        }
    }

    fun agregarMascota(m: MascotaEncontrada) {
        viewModelScope.launch {
            val uid = auth.currentUser?.uid ?: return@launch
            db.collection("usuarios").document(uid)
                .collection("mascotas_encontradas")
                .add(m)
                .addOnSuccessListener { _lista.value = listOf(m) + _lista.value }
        }
    }
}