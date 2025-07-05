package com.proyecto.appmascotassos4.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.proyecto.appmascotassos4.model.Mascota
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FirestoreViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _listaMascotas = MutableStateFlow<List<Mascota>>(emptyList())
    val listaMascotas: StateFlow<List<Mascota>> = _listaMascotas

    fun guardarMascota(
        mascota: Mascota,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {
        val userId = auth.currentUser?.uid ?: return onError(Exception("Usuario no autenticado"))
        db.collection("usuarios")
            .document(userId)
            .collection("mascotas")
            .add(mascota)
            .addOnSuccessListener {
                obtenerMascotasDelUsuario()
                onSuccess()
            }
            .addOnFailureListener { onError(it) }
    }

    fun obtenerMascotasDelUsuario() {
        val userId = auth.currentUser?.uid ?: return
        db.collection("usuarios")
            .document(userId)
            .collection("mascotas")
            .get()
            .addOnSuccessListener { result ->
                val mascotas = result.map { doc ->
                    doc.toObject(Mascota::class.java).copy(id = doc.id)
                }
                _listaMascotas.value = mascotas
            }
    }

    fun actualizarMascota(mascota: Mascota) {
        val userId = auth.currentUser?.uid ?: return
        val mascotaId = mascota.id ?: return
        db.collection("usuarios")
            .document(userId)
            .collection("mascotas")
            .document(mascotaId)
            .set(mascota)
            .addOnSuccessListener {
                obtenerMascotasDelUsuario()
            }
    }

    fun eliminarMascota(mascotaId: String) {
        val userId = auth.currentUser?.uid ?: return
        db.collection("usuarios")
            .document(userId)
            .collection("mascotas")
            .document(mascotaId)
            .delete()
            .addOnSuccessListener {
                obtenerMascotasDelUsuario()
            }
    }
}