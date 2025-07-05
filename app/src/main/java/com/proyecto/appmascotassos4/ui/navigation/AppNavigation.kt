package com.proyecto.appmascotassos4.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.proyecto.appmascotassos4.R
import com.proyecto.appmascotassos4.ui.*
import com.proyecto.appmascotassos4.ui.MascotaPerdidaScreen

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = stringResource(R.string.splash)) {

        composable("splash")             { SplashScreen(navController) }
        composable("login")              { LoginScreen(navController) }
        composable("registro")           { RegistroScreen(navController) }
        composable("registro_dueño")     { RegistroDueñoScreen(navController) }
        composable("mi_perfil")          { MiPerfilScreen() }
        composable("mis_mascotas")       { MisMascotasScreen() }
        composable("principal")          { PantallaPrincipalScreen(navController) }
        composable("reporte_encontrada") { ReporteMascotaEncontradaScreen(navController) }
        composable("coincidencias")      { CoincidenciasScreen() }
        composable("lista_mis_mascotas") { ListaMisMascotasScreen() }
        composable("mascota_perdida")    { MascotaPerdidaScreen(navController) }
        composable("mapa_mascotas_cercanas") { MapaMascotasCercanasScreen(navController) }
    }
}
