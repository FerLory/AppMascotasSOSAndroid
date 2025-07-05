package com.proyecto.appmascotassos4.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.proyecto.appmascotassos4.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.InputStreamReader

class RazaViewModel : ViewModel() {

    private val _razas = MutableStateFlow<List<String>>(emptyList())
    val razas: StateFlow<List<String>> = _razas

    fun cargarRazas(context: Context, especie: String) {
        val perro = context.getString(R.string.perro)
        val dog = context.getString(R.string.dog)
        val gato = context.getString(R.string.gato)
        val cat = context.getString(R.string.cat)

        val archivo = when (especie.lowercase()) {
            perro.lowercase(), dog.lowercase() -> "dog_breeds.json"
            gato.lowercase(), cat.lowercase() -> "cat_breeds.json"
            else -> null
        } ?: return

        val input = context.assets.open(archivo)
        val reader = InputStreamReader(input)
        _razas.value = Gson().fromJson(reader, Array<String>::class.java).toList()
    }
}