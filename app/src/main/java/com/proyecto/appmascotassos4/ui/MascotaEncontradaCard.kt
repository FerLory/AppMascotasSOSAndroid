package com.proyecto.appmascotassos4.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.proyecto.appmascotassos4.R
import com.proyecto.appmascotassos4.model.MascotaEncontrada

@Composable
fun MascotaEncontradaCard(
    mascota: MascotaEncontrada,
    onClick: () -> Unit = {}
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
    ) {
        Row(Modifier.padding(16.dp)) {
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
                Text(mascota.nombre)
                Text("${stringResource(R.string.raza)}: ${mascota.raza}")
                Text("${stringResource(R.string.senas)}: ${mascota.senas}")
                Text("${stringResource(R.string.fecha_hora)}: ${mascota.fechaHora}")
            }
        }
    }
}