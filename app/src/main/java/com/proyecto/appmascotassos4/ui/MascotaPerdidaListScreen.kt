package com.proyecto.appmascotassos4.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.proyecto.appmascotassos4.model.MascotaPerdida

@Composable
fun MascotaPerdidaListScreen(lista: List<MascotaPerdida>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(lista) { m ->
            MascotaPerdidaCard(mascota = m)
        }
    }
}