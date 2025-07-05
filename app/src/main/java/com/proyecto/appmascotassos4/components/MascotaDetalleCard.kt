package com.proyecto.appmascotassos4.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.proyecto.appmascotassos4.R
import com.proyecto.appmascotassos4.model.Mascota

@Composable
fun MascotaDetalleCard(
    mascota: Mascota,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
    ) {
        Column {
            Image(
                painter = rememberAsyncImagePainter(mascota.fotoUrl),
                contentDescription = mascota.nombre,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            )
            Column(Modifier.padding(16.dp)) {
                Text(mascota.nombre, style = MaterialTheme.typography.titleMedium)
                Text(stringResource(R.string.raza, mascota.raza))
                Text(stringResource(R.string.edad, mascota.edad))
                if (mascota.descripcion.isNotBlank()) {
                    Text(mascota.descripcion, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}