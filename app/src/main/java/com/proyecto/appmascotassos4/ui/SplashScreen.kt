package com.proyecto.appmascotassos4.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.proyecto.appmascotassos4.R
import com.proyecto.appmascotassos4.datastore.UserPreferences
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await

@SuppressLint("StaticFieldLeak")
@Composable
fun SplashScreen(navController: NavController) {

    val context   = LocalContext.current
    val prefs     = remember { UserPreferences(context) }

    LaunchedEffect(Unit) {

        val auth  = FirebaseAuth.getInstance()
        val user  = auth.currentUser


        delay(1200)

        if (user == null) {

            navController.navigate("login") { popUpTo("splash"){ inclusive = true } }
            return@LaunchedEffect
        }


        val stillValid = try {
            user.reload().await()
            true
        } catch (e: Exception) {
            false
        }

        if (!stillValid) {
            auth.signOut()
            navController.navigate("login") { popUpTo("splash"){ inclusive = true } }
            return@LaunchedEffect
        }


        val registroCompleto = prefs.obtenerRegistroCompleto().first()

        navController.navigate(
            if (registroCompleto) "principal" else "registro_dueño"
        ) { popUpTo("splash"){ inclusive = true } }
    }


    Column(
        Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(R.drawable.logo_splash),
            contentDescription = null,
            modifier = Modifier.size(180.dp)
        )
        Spacer(Modifier.height(32.dp))
        CircularProgressIndicator()
    }
}