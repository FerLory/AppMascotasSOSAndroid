package com.proyecto.appmascotassos4.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.android.gms.maps.model.LatLng
import com.proyecto.appmascotassos4.R
import com.proyecto.appmascotassos4.components.SeleccionMapaComposable
import com.proyecto.appmascotassos4.model.MascotaEncontrada
import com.proyecto.appmascotassos4.viewmodel.MascotaEncontradaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReporteMascotaEncontradaScreen(
    navController: NavHostController,
    viewModel: MascotaEncontradaViewModel = viewModel()
) {
    val context = LocalContext.current

    var nombre by remember { mutableStateOf("") }
    var especie by remember { mutableStateOf("") }
    var raza by remember { mutableStateOf("") }
    var senas by remember { mutableStateOf("") }
    var fechaHora by remember { mutableStateOf("") }
    var fotoUrl by remember { mutableStateOf("") }
    var ubicacion by remember { mutableStateOf<LatLng?>(null) }
    var direccion by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.mascota_encontrada)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.atras)
                        )
                    }
                }
            )
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner)
                .padding(16.dp)
        ) {
            Text(stringResource(R.string.reportar_mascota_encontrada), fontSize = 20.sp)
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text(stringResource(R.string.nombre)) },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = especie,
                onValueChange = { especie = it },
                label = { Text(stringResource(R.string.especie)) },
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
                label = { Text(stringResource(R.string.senas_particulares)) },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = fechaHora,
                onValueChange = { fechaHora = it },
                label = { Text(stringResource(R.string.fecha_y_hora)) },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = fotoUrl,
                onValueChange = { fotoUrl = it },
                label = { Text(stringResource(R.string.url_foto)) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            SeleccionMapaComposable { latLng, dir ->
                ubicacion = latLng
                direccion = dir
            }

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    val latLng = ubicacion
                    if (
                        nombre.isBlank() || especie.isBlank() || raza.isBlank() ||
                        senas.isBlank() || fechaHora.isBlank() || fotoUrl.isBlank() || latLng == null
                    ) {
                        Toast.makeText(
                            context,
                            context.getString(R.string.completa_todos_los_campos),
                            Toast.LENGTH_SHORT
                        ).show()
                        return@Button
                    }

                    val mascota = MascotaEncontrada(
                        nombre = nombre,
                        especie = especie,
                        raza = raza,
                        senas = senas,
                        fechaHora = fechaHora,
                        fotoUrl = fotoUrl,
                        colonia = "",
                        direccion = direccion,
                        latitud = latLng.latitude,
                        longitud = latLng.longitude
                    )

                    viewModel.agregarMascota(mascota)
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.guardar))
            }
        }
    }
}