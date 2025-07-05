package com.proyecto.appmascotassos4.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.android.gms.maps.model.LatLng
import com.proyecto.appmascotassos4.R
import com.proyecto.appmascotassos4.components.SeleccionMapaComposable
import com.proyecto.appmascotassos4.viewmodel.DuenoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroDueñoScreen(
    navController: NavController,
    viewModel: DuenoViewModel = viewModel()
) {
    val context = LocalContext.current

    var nombre by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var ubicacion by remember { mutableStateOf<LatLng?>(null) }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Text(stringResource(R.string.registro_dueno), style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(nombre, { nombre = it }, label = { Text(stringResource(R.string.nombre)) }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(telefono, { telefono = it }, label = { Text(stringResource(R.string.telefono)) }, modifier = Modifier.fillMaxWidth())

        Spacer(Modifier.height(16.dp))

        SeleccionMapaComposable { latLng, dir ->
            ubicacion = latLng
            direccion = dir
        }

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                val latLng = ubicacion
                if (nombre.isNotBlank() && telefono.isNotBlank() && direccion.isNotBlank() && latLng != null) {
                    viewModel.guardarDatosDueño(
                        nombre = nombre,
                        telefono = telefono,
                        direccion = direccion,
                        latitud = latLng.latitude,
                        longitud = latLng.longitude
                    )
                    navController.navigate("principal")
                } else {
                    Toast.makeText(context, context.getString(R.string.completa_todos_los_campos), Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.guardar))
        }
    }
}