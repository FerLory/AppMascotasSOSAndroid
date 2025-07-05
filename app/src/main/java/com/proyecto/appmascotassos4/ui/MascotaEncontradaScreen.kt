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
import androidx.core.net.toFile
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.proyecto.appmascotassos4.R
import com.proyecto.appmascotassos4.components.SeleccionMapaComposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL
import javax.net.ssl.HttpsURLConnection
import java.util.*

@Composable
fun MascotaEncontradaScreen(navController: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var nombre by remember { mutableStateOf("") }
    var especie by remember { mutableStateOf("") }
    var raza by remember { mutableStateOf("") }
    var senas by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var latitud by remember { mutableStateOf(0.0) }
    var longitud by remember { mutableStateOf(0.0) }

    var codigoPostal by remember { mutableStateOf("") }
    var coloniaSeleccionada by remember { mutableStateOf("") }
    var colonias by remember { mutableStateOf(listOf<String>()) }

    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val scrollState = rememberScrollState()
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Text(stringResource(R.string.reporte_mascota_encontrada), style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text(stringResource(R.string.nombre)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = especie,
            onValueChange = { especie = it },
            label = { Text(stringResource(R.string.especie)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = raza,
            onValueChange = { raza = it },
            label = { Text(stringResource(R.string.raza)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = senas,
            onValueChange = { senas = it },
            label = { Text(stringResource(R.string.senas_particulares)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = codigoPostal,
            onValueChange = {
                codigoPostal = it
                if (codigoPostal.length == 5) {
                    scope.launch {
                        try {
                            val resultado = withContext(Dispatchers.IO) {
                                val url = URL("https://api-codigos-postales-mx.vercel.app/codigo/$codigoPostal")
                                val con = url.openConnection() as HttpsURLConnection
                                con.requestMethod = "GET"
                                con.connect()
                                val stream = con.inputStream.bufferedReader().readText()
                                JSONObject(stream)
                            }
                            val coloniasArray = resultado.getJSONArray("colonias")
                            val lista = mutableListOf<String>()
                            for (i in 0 until coloniasArray.length()) {
                                lista.add(coloniasArray.getString(i))
                            }
                            colonias = lista
                        } catch (e: Exception) {
                            colonias = listOf()
                        }
                    }
                }
            },
            label = { Text(stringResource(R.string.codigo_postal)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (colonias.isNotEmpty()) {
            DropdownMenu(
                expanded = true,
                onDismissRequest = {},
                modifier = Modifier.fillMaxWidth()
            ) {
                colonias.forEach { colonia ->
                    DropdownMenuItem(
                        text = { Text(colonia) },
                        onClick = {
                            coloniaSeleccionada = colonia
                            direccion = "$colonia, CP $codigoPostal"
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(stringResource(R.string.selecciona_ubicacion_mapa))
        Spacer(modifier = Modifier.height(8.dp))

        SeleccionMapaComposable { latLng: LatLng, direccionSeleccionada: String ->
            latitud = latLng.latitude
            longitud = latLng.longitude
            direccion = direccionSeleccionada
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { imagePickerLauncher.launch("image/*") }) {
            Text(stringResource(R.string.seleccionar_foto))
        }

        Spacer(modifier = Modifier.height(8.dp))

        imageUri?.let {
            Image(
                painter = rememberAsyncImagePainter(it),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (nombre.isBlank() || especie.isBlank() || raza.isBlank() || senas.isBlank() || direccion.isBlank() || imageUri == null) {
                    Toast.makeText(context, context.getString(R.string.completa_todos_los_campos), Toast.LENGTH_SHORT).show()
                } else {
                    val userId = FirebaseAuth.getInstance().currentUser?.uid
                    if (userId != null) {
                        val storageRef = FirebaseStorage.getInstance().reference
                        val fileName = UUID.randomUUID().toString()
                        val imageRef = storageRef.child("mascotas_encontradas/$userId/$fileName.jpg")
                        imageUri?.let { uri ->
                            imageRef.putFile(uri).addOnSuccessListener {
                                imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                                    val db = FirebaseFirestore.getInstance()
                                    val datos = hashMapOf(
                                        "nombre" to nombre,
                                        "especie" to especie,
                                        "raza" to raza,
                                        "senas" to senas,
                                        "direccion" to direccion,
                                        "latitud" to latitud,
                                        "longitud" to longitud,
                                        "fotoUrl" to downloadUri.toString()
                                    )
                                    db.collection("usuarios").document(userId)
                                        .collection("mascotas_encontradas")
                                        .add(datos)
                                        .addOnSuccessListener {
                                            navController.navigate("principal") {
                                                popUpTo("mascota_encontrada") { inclusive = true }
                                            }
                                        }
                                }
                            }
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Text(stringResource(R.string.guardar_mascota_encontrada))
        }
    }
}