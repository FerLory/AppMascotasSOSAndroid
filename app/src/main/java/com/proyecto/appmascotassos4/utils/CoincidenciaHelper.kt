package com.proyecto.appmascotassos4.utils

import kotlin.math.*
import com.proyecto.appmascotassos4.model.MascotaEncontrada
import com.proyecto.appmascotassos4.model.MascotaPerdida

object CoincidenciaHelper {

    fun buscarCoincidencias(
        mascota: MascotaEncontrada,
        mascotasPerdidas: List<MascotaPerdida>
    ): List<MascotaPerdida> {
        return mascotasPerdidas.filter {
            it.raza.equals(mascota.raza, true) &&
                    distanciaKm(it.latitud, it.longitud, mascota.latitud, mascota.longitud) <= 5.0 &&
                    tienePalabrasClaveEnComun(it.senas, mascota.senas)
        }
    }

    private fun distanciaKm(
        lat1: Double, lon1: Double,
        lat2: Double, lon2: Double
    ): Double {
        val r = 6371.0
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2).pow(2) + cos(Math.toRadians(lat1)) *
                cos(Math.toRadians(lat2)) * sin(dLon / 2).pow(2)
        return 2 * r * asin(sqrt(a))
    }

    private fun tienePalabrasClaveEnComun(t1: String, t2: String): Boolean {
        val p1 = t1.lowercase().split(" ", ",", ".", ";").map { it.trim() }.filter { it.isNotEmpty() }.toSet()
        val p2 = t2.lowercase().split(" ", ",", ".", ";").map { it.trim() }.filter { it.isNotEmpty() }.toSet()
        return p1.intersect(p2).isNotEmpty()
    }
}