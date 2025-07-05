package com.proyecto.appmascotassos4.ui

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.google.android.gms.maps.model.LatLng
import com.proyecto.appmascotassos4.R
import com.proyecto.appmascotassos4.components.SeleccionMapaComposable
import com.proyecto.appmascotassos4.model.MascotaPerdida
import com.proyecto.appmascotassos4.viewmodel.MascotaPerdidaViewModel
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MascotaPerdidaScreen(
    navController: NavController,
    viewModel: MascotaPerdidaViewModel = viewModel()
) {
    val context = LocalContext.current
    val scroll = rememberScrollState()

    var nombre by remember { mutableStateOf("") }
    var raza by remember { mutableStateOf("") }
    var senas by remember { mutableStateOf("") }
    var fechaHora by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var latitud by remember { mutableStateOf(0.0) }
    var longitud by remember { mutableStateOf(0.0) }
    var fotoUri by remember { mutableStateOf<Uri?>(null) }

    val imgLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> fotoUri = uri }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scroll)
            .padding(16.dp)
    ) {
        Text(stringResource(R.string.reporte_mascota_perdida), style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text(stringResource(R.string.nombre)) },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = raza,
            onValueChange = { raza = it },
            label = { Text(stringResource(R.string.raza)) },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = senas,
            onValueChange = { senas = it },
            label = { Text(stringResource(R.string.senas)) },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = fechaHora,
            onValueChange = { fechaHora = it },
            label = { Text(stringResource(R.string.fecha_hora)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        Text(stringResource(R.string.selecciona_ultima_ubicacion))
        Spacer(Modifier.height(8.dp))

        SeleccionMapaComposable { latLng: LatLng, dir: String ->
            latitud = latLng.latitude
            longitud = latLng.longitude
            direccion = dir
        }

        Spacer(Modifier.height(16.dp))

        Button(onClick = { imgLauncher.launch("image/*") }) {
            Text(stringResource(R.string.seleccionar_foto))
        }

        fotoUri?.let {
            Spacer(Modifier.height(8.dp))
            Image(
                painter = rememberAsyncImagePainter(it),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
        }

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                if (nombre.isBlank() || raza.isBlank() || senas.isBlank() || fechaHora.isBlank() || direccion.isBlank()) {
                    Toast.makeText(context, context.getString(R.string.completa_todos_los_campos), Toast.LENGTH_SHORT).show()
                    return@Button
                }
                val mascota = MascotaPerdida(
                    nombre = nombre,
                    raza = raza,
                    senas = senas,
                    fechaHora = fechaHora,
                    fotoUrl = fotoUri?.toString() ?: "",
                    latitud = latitud,
                    longitud = longitud,
                    direccion = direccion
                )
                viewModel.agregarMascota(
                    mascota,
                    onSuccess = { navController.popBackStack() },
                    onError = { e -> Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show() }
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(stringResource(R.string.guardar_mascota_perdida))
        }
    }
}