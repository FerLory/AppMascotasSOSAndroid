package com.proyecto.appmascotassos4.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.proyecto.appmascotassos4.R
import com.proyecto.appmascotassos4.viewmodel.PerfilViewModel

@Composable
fun MiPerfilScreen() {
    val viewModel: PerfilViewModel = viewModel()
    val datos by viewModel.datosDueño.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(stringResource(R.string.mi_perfil), style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(24.dp))

        datos?.let {
            Text("${stringResource(R.string.nombre)}: ${it.nombre}")
            Spacer(modifier = Modifier.height(8.dp))
            Text("${stringResource(R.string.correo)}: ${it.correo}")
            Spacer(modifier = Modifier.height(8.dp))
            Text("${stringResource(R.string.telefono)}: ${it.telefono}")
            Spacer(modifier = Modifier.height(8.dp))
            Text("${stringResource(R.string.direccion)}: ${it.direccion}")
        } ?: run {
            CircularProgressIndicator()
        }
    }
}