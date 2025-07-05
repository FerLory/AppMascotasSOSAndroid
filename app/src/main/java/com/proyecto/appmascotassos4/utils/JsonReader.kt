package com.proyecto.appmascotassos4.utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.proyecto.appmascotassos4.model.MascotaEncontrada
import com.proyecto.appmascotassos4.model.MascotaPerdida
import java.io.InputStreamReader


object JsonReader {

    fun leerMascotasPerdidas(context: Context): List<MascotaPerdida> {
        val input = context.assets.open("mascotas_perdidas.json")
        val reader = InputStreamReader(input)
        val type   = object : TypeToken<List<MascotaPerdida>>() {}.type
        return Gson().fromJson(reader, type)
    }

    fun leerMascotasEncontradas(context: Context): List<MascotaEncontrada> {
        val input = context.assets.open("mascotas_encontradas.json")
        val reader = InputStreamReader(input)
        val type   = object : TypeToken<List<MascotaEncontrada>>() {}.type
        return Gson().fromJson(reader, type)
    }
}