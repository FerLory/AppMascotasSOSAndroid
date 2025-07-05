package com.proyecto.appmascotassos4.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.proyecto.appmascotassos4.R
import com.proyecto.appmascotassos4.viewmodel.FirestoreViewModel
import com.proyecto.appmascotassos4.model.Mascota
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Composable
fun ListaMisMascotasScreen(viewModel: FirestoreViewModel = viewModel()) {
    val mascotas by viewModel.listaMascotas.collectAsState()
    val edicionActiva = remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        viewModel.obtenerMascotasDelUsuario()
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(stringResource(R.string.mis_mascotas_registradas), style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(mascotas) { mascota ->
                var nombre by remember { mutableStateOf(mascota.nombre) }
                var especie by remember { mutableStateOf(mascota.especie) }
                var raza by remember { mutableStateOf(mascota.raza) }
                var edad by remember { mutableStateOf(mascota.edad) }
                var sexo by remember { mutableStateOf(mascota.sexo) }
                var senas by remember { mutableStateOf(mascota.senas) }

                val enEdicion = edicionActiva.value == mascota.id

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        if (enEdicion) {
                            OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text(stringResource(R.string.nombre)) })
                            OutlinedTextField(value = especie, onValueChange = { especie = it }, label = { Text(stringResource(R.string.especie)) })
                            OutlinedTextField(value = raza, onValueChange = { raza = it }, label = { Text(stringResource(R.string.raza)) })
                            OutlinedTextField(value = edad, onValueChange = { edad = it }, label = { Text(stringResource(R.string.edad)) })
                            OutlinedTextField(value = sexo, onValueChange = { sexo = it }, label = { Text(stringResource(R.string.sexo)) })
                            OutlinedTextField(value = senas, onValueChange = { senas = it }, label = { Text(stringResource(R.string.senas)) })

                            Spacer(modifier = Modifier.height(8.dp))

                            Button(onClick = {
                                val mascotaEditada = mascota.copy(
                                    nombre = nombre,
                                    especie = especie,
                                    raza = raza,
                                    edad = edad,
                                    sexo = sexo,
                                    senas = senas
                                )
                                viewModel.actualizarMascota(mascotaEditada)
                                edicionActiva.value = null
                            }) {
                                Icon(Icons.Default.Save, contentDescription = stringResource(R.string.guardar))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(stringResource(R.string.guardar))
                            }
                        } else {
                            Text("${stringResource(R.string.nombre)}: ${mascota.nombre}")
                            Text("${stringResource(R.string.especie)}: ${mascota.especie}")
                            Text("${stringResource(R.string.raza)}: ${mascota.raza}")
                            Text("${stringResource(R.string.edad)}: ${mascota.edad}")
                            Text("${stringResource(R.string.sexo)}: ${mascota.sexo}")
                            Text("${stringResource(R.string.senas)}: ${mascota.senas}")

                            Spacer(modifier = Modifier.height(8.dp))

                            Row {
                                Button(onClick = { edicionActiva.value = mascota.id }, modifier = Modifier.weight(1f)) {
                                    Icon(Icons.Default.Edit, contentDescription = stringResource(R.string.editar))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(stringResource(R.string.editar))
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Button(
                                    onClick = { viewModel.eliminarMascota(mascota.id ?: "") },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                                ) {
                                    Icon(Icons.Default.Delete, contentDescription = stringResource(R.string.eliminar))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(stringResource(R.string.eliminar))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}