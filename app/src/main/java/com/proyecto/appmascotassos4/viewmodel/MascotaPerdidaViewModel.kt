package com.proyecto.appmascotassos4.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.proyecto.appmascotassos4.model.MascotaPerdida
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.proyecto.appmascotassos4.R

class MascotaPerdidaViewModel(application: Application) : AndroidViewModel(application) {

    private val db = FirebaseFirestore.getInstance()
    private val context = getApplication<Application>().applicationContext

    private val _lista = MutableStateFlow<List<MascotaPerdida>>(emptyList())
    val lista = _lista.asStateFlow()

    private var lastVisible: DocumentSnapshot? = null
    private var isLoading = false
    private val pageSize = 10

    init { cargarMas() }

    fun cargarMas() {
        if (isLoading) return
        isLoading = true
        viewModelScope.launch {
            val base = db.collection("mascotas_perdidas")
                .orderBy(context.getString(R.string.campo_fecha_hora))
                .limit(pageSize.toLong())

            val query = if (lastVisible != null) base.startAfter(lastVisible!!) else base
            query.get().addOnSuccessListener { snap ->
                if (!snap.isEmpty) {
                    lastVisible = snap.documents.last()
                    val nuevas = snap.documents.map { d ->
                        MascotaPerdida(
                            nombre     = d.getString(context.getString(R.string.campo_nombre)) ?: "",
                            raza       = d.getString(context.getString(R.string.campo_raza)) ?: "",
                            senas      = d.getString(context.getString(R.string.campo_senas)) ?: "",
                            fechaHora  = d.getString(context.getString(R.string.campo_fecha_hora)) ?: "",
                            fotoUrl    = d.getString(context.getString(R.string.campo_foto_url)) ?: "",
                            latitud    = d.getDouble(context.getString(R.string.campo_latitud)) ?: 0.0,
                            longitud   = d.getDouble(context.getString(R.string.campo_longitud)) ?: 0.0,
                            direccion  = d.getString(context.getString(R.string.campo_direccion)) ?: ""
                        )
                    }
                    _lista.value = _lista.value + nuevas
                }
                isLoading = false
            }.addOnFailureListener { isLoading = false }
        }
    }

    fun agregarMascota(
        mascota : MascotaPerdida,
        onSuccess: () -> Unit = {},
        onError  : (Exception) -> Unit = {}
    ) {
        val data = hashMapOf(
            context.getString(R.string.campo_nombre)    to mascota.nombre,
            context.getString(R.string.campo_raza)      to mascota.raza,
            context.getString(R.string.campo_senas)     to mascota.senas,
            context.getString(R.string.campo_fecha_hora) to mascota.fechaHora,
            context.getString(R.string.campo_foto_url)   to mascota.fotoUrl,
            context.getString(R.string.campo_direccion) to mascota.direccion,
            context.getString(R.string.campo_latitud)   to mascota.latitud,
            context.getString(R.string.campo_longitud)  to mascota.longitud
        )

        db.collection("mascotas_perdidas")
            .add(data)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener  { onError(it) }
    }
}