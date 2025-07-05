package com.proyecto.appmascotassos4.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.proyecto.appmascotassos4.R
import com.proyecto.appmascotassos4.model.MascotaEncontrada
import com.proyecto.appmascotassos4.viewmodel.MascotaCoincidenciaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoincidenciasScreen(viewModel: MascotaCoincidenciaViewModel = viewModel()) {
    val coincidencias by viewModel.coincidencias.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text(stringResource(R.string.coincidencias)) }) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(coincidencias) { mascota: MascotaEncontrada ->
                Card(Modifier.fillMaxWidth().padding(bottom = 12.dp)) {
                    Column(Modifier.padding(16.dp)) {
                        Text("${stringResource(R.string.nombre)}: ${mascota.nombre}")
                        Text("${stringResource(R.string.raza)}: ${mascota.raza}")
                        Text("${stringResource(R.string.senas)}: ${mascota.senas}")
                        Text("${stringResource(R.string.fecha_hora)}: ${mascota.fechaHora}")
                    }
                }
            }
        }
    }
}