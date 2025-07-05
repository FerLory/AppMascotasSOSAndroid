package com.proyecto.appmascotassos4.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.proyecto.appmascotassos4.R
import com.proyecto.appmascotassos4.model.Mascota
import com.proyecto.appmascotassos4.viewmodel.FirestoreViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MisMascotasScreen(viewModel: FirestoreViewModel = viewModel()) {


    var nombre  by remember { mutableStateOf("") }
    var especie by remember { mutableStateOf("") }
    var raza    by remember { mutableStateOf("") }
    var edad    by remember { mutableStateOf("") }
    var sexo    by remember { mutableStateOf("") }
    var senas   by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }


    val ctx = LocalContext.current

    Column(Modifier.padding(16.dp)) {

        Text(stringResource(R.string.registro_mascota), style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(nombre,  { nombre  = it }, label = { Text(stringResource(R.string.nombre))  }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(especie, { especie = it }, label = { Text(stringResource(R.string.especie)) }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(raza,    { raza    = it }, label = { Text(stringResource(R.string.raza))    }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(
            edad, { edad = it },
            label = { Text(stringResource(R.string.edad)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(sexo, { sexo = it }, label = { Text(stringResource(R.string.sexo)) }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(senas,{ senas= it },label = { Text(stringResource(R.string.senas_particulares)) },modifier = Modifier.fillMaxWidth())

        Spacer(Modifier.height(16.dp))

        Row {


            Button(
                onClick = {
                    val mascota = Mascota(nombre, especie, raza, edad, sexo, senas)
                    viewModel.guardarMascota(
                        mascota,
                        onSuccess = {
                            mensaje = ctx.getString(R.string.mascota_guardada)
                            nombre = ""; especie = ""; raza = ""; edad = ""; sexo = ""; senas = ""
                        },
                        onError = { e ->
                            mensaje = ctx.getString(R.string.error_guardar) + " ${e.message}"
                        }
                    )
                },
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.Save, contentDescription = stringResource(R.string.guardar))
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.guardar))
            }

            Spacer(Modifier.width(8.dp))

            Button(
                onClick = {
                    nombre = ""; especie = ""; raza = ""; edad = ""; sexo = ""; senas = ""; mensaje = ""
                },
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.nuevo))
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.nuevo))
            }
        }

        if (mensaje.isNotEmpty()) {
            Spacer(Modifier.height(16.dp))
            Text(mensaje, color = MaterialTheme.colorScheme.primary)
        }
    }
}