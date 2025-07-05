package com.proyecto.appmascotassos4.model

data class MascotaPerdida(
    val nombre: String = "",
    val raza: String = "",
    val senas: String = "",
    val fechaHora: String = "",
    val fotoUrl: String = "",
    val latitud: Double = 0.0,
    val longitud: Double = 0.0,
    val direccion: String = "",
    val id: String? = null
)