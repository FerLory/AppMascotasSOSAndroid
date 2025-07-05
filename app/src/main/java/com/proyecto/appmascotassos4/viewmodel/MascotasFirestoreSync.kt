package com.proyecto.appmascotassos4.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.proyecto.appmascotassos4.R
import com.proyecto.appmascotassos4.model.MascotaEncontrada
import com.proyecto.appmascotassos4.model.MascotaPerdida
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MascotasFirestoreSync(application: Application) : AndroidViewModel(application) {

    private val db = FirebaseFirestore.getInstance()
    private val context = getApplication<Application>().applicationContext

    private val _perdidas = MutableStateFlow<List<MascotaPerdida>>(emptyList())
    val perdidas: StateFlow<List<MascotaPerdida>> = _perdidas

    private val _encontradas = MutableStateFlow<List<MascotaEncontrada>>(emptyList())
    val encontradas: StateFlow<List<MascotaEncontrada>> = _encontradas

    init {
        db.collectionGroup(context.getString(R.string.coleccion_mascotas_perdidas))
            .addSnapshotListener { snap, _ ->
                _perdidas.value = snap?.mapNotNull { it.toObject(MascotaPerdida::class.java) } ?: emptyList()
            }
        db.collectionGroup(context.getString(R.string.coleccion_mascotas_encontradas))
            .addSnapshotListener { snap, _ ->
                _encontradas.value = snap?.mapNotNull { it.toObject(MascotaEncontrada::class.java) } ?: emptyList()
            }
    }
}