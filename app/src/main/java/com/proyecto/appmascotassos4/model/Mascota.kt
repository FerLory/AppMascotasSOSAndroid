package com.proyecto.appmascotassos4.model

data class Mascota(
    val nombre: String = "",
    val especie: String = "",
    val raza: String = "",
    val edad: String = "",
    val sexo: String = "",
    val senas: String = "",
    val descripcion: String = "",
    val fotoUrl: String = "",
    val id: String? = null
)