package com.proyecto.appmascotassos4.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _authStatus = MutableLiveData<Boolean?>()
    val authStatus: LiveData<Boolean?> = _authStatus

    fun registrarUsuario(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                _authStatus.value = task.isSuccessful
            }
    }

    fun iniciarSesion(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                _authStatus.value = task.isSuccessful
            }
    }

    fun cerrarSesion() {
        auth.signOut()
        _authStatus.value = false
    }

    fun limpiarEstado() {
        _authStatus.value = null
    }
}
