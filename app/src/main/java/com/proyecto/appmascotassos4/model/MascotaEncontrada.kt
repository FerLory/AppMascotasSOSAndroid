package com.proyecto.appmascotassos4.model

data class MascotaEncontrada(
    val nombre: String = "",
    val especie: String = "",
    val raza: String = "",
    val senas: String = "",
    val fechaHora: String = "",
    val fotoUrl: String = "",
    val colonia: String = "",
    val direccion: String = "",
    val latitud: Double = 0.0,
    val longitud: Double = 0.0,
    val id: String? = null
)