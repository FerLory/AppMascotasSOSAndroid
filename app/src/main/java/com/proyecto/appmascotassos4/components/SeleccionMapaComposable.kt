package com.proyecto.appmascotassos4.components

import android.annotation.SuppressLint
import android.location.Geocoder
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.*
import java.util.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.proyecto.appmascotassos4.R

@SuppressLint("MissingPermission")
@Composable
fun SeleccionMapaComposable(
    onUbicacionSeleccionada: (LatLng, String) -> Unit
) {
    val context = LocalContext.current
    val defaultLatLng = LatLng(19.4326, -99.1332) // CDMX

    var posicionSeleccionada by remember { mutableStateOf<LatLng?>(null) }
    var direccionSeleccionada by remember { mutableStateOf("") }
    var direccionBusqueda by remember { mutableStateOf("") }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultLatLng, 12f)
    }

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)) {
        OutlinedTextField(
            value = direccionBusqueda,
            onValueChange = { direccionBusqueda = it },
            label = { Text("Buscar dirección") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                val geocoder = Geocoder(context, Locale.getDefault())
                try {
                    val resultados = geocoder.getFromLocationName(direccionBusqueda, 1)
                    if (!resultados.isNullOrEmpty()) {
                        val location = resultados[0]
                        val nuevaPosicion = LatLng(location.latitude, location.longitude)
                        posicionSeleccionada = nuevaPosicion
                        direccionSeleccionada = direccionBusqueda
                        onUbicacionSeleccionada(nuevaPosicion, direccionBusqueda)

                        cameraPositionState.move(CameraUpdateFactory.newLatLngZoom(nuevaPosicion, 15f))
                    }
                } catch (e: Exception) {
                    Log.e("MapSearch", "Error buscando direccion", e)
                }
            },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Buscar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(modifier = Modifier
            .height(300.dp)
            .fillMaxWidth()) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                onMapClick = { latLng ->
                    posicionSeleccionada = latLng
                    direccionSeleccionada = obtenerDireccion(context, latLng)
                    onUbicacionSeleccionada(latLng, direccionSeleccionada)
                }
            ) {
                posicionSeleccionada?.let {
                    Marker(
                        state = MarkerState(position = it),
                        title = "Ubicación seleccionada",
                        snippet = direccionSeleccionada
                    )
                }
            }
        }

        if (direccionSeleccionada.isNotEmpty()) {
            Text(
                text = "Dirección: $direccionSeleccionada",
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

fun obtenerDireccion(context: android.content.Context, latLng: LatLng): String {
    return try {
        val geocoder = Geocoder(context, Locale.getDefault())
        val result = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
        if (!result.isNullOrEmpty()) {
            val d = result[0]
            listOfNotNull(d.thoroughfare, d.subLocality, d.locality, d.adminArea)
                .joinToString(", ")
        } else {
            context.getString(R.string.direcci_n_no_encontrada)
        }
    } catch (e: Exception) {
        context.getString(R.string.error_obteniendo_direcci_n)
    }
}
