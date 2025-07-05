package com.proyecto.appmascotassos4.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.proyecto.appmascotassos4.model.MascotaPerdida
import com.proyecto.appmascotassos4.viewmodel.MascotasViewModel
import com.proyecto.appmascotassos4.viewmodel.UbicacionViewModel

@Composable
fun MascotasCercanasScreen(
    navController: NavHostController,
    mascotasViewModel: MascotasViewModel = viewModel(),
    ubicacionViewModel: UbicacionViewModel = viewModel()
) {
    // ubicación actual del usuario (lat, lon)
    val ubicacion by ubicacionViewModel.ubicacion.collectAsState()
    // listado reactivo de mascotas dentro de 5 km
    val mascotas by mascotasViewModel.mascotasCercanas.collectAsState()

    /*  Cada vez que cambie la ubicación del usuario
        (o la pantalla se recoponga) avisamos al viewModel
        para que recalcule las coincidencias de cercanía. */
    LaunchedEffect(ubicacion) {
        mascotasViewModel.setUbicacionUsuario(
            lat = ubicacion.first,
            lon = ubicacion.second
        )
    }

    when {
        mascotas.isEmpty() -> { // aún cargando o no hay resultados
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        else -> {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                items(mascotas) { mascota ->
                    MascotaCard(mascota) {

                    }
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun MascotaCard(
    mascota: MascotaPerdida,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = mascota.fotoUrl,
                contentDescription = mascota.nombre,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(64.dp)
                    .clip(MaterialTheme.shapes.small)
            )
            Spacer(Modifier.width(12.dp))
            Column {
                Text(mascota.nombre ?: "Sin nombre",
                    style = MaterialTheme.typography.titleMedium)
                Text(mascota.raza ?: "",
                    style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
