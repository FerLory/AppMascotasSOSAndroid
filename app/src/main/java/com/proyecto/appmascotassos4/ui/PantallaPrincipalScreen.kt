package com.proyecto.appmascotassos4.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.proyecto.appmascotassos4.R
import com.proyecto.appmascotassos4.viewmodel.MascotaPerdidaViewModel
import com.proyecto.appmascotassos4.viewmodel.MascotaEncontradaViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaPrincipalScreen(navController: NavController) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val perdidasVM: MascotaPerdidaViewModel = viewModel()
    val encontradasVM: MascotaEncontradaViewModel = viewModel()

    val perdidas by perdidasVM.lista.collectAsState()
    val encontradas by encontradasVM.lista.collectAsState()

    var selectedTab by remember { mutableStateOf(0) }
    val tabTitles = listOf(
        stringResource(R.string.perdidas),
        stringResource(R.string.encontradas),
        stringResource(R.string.sos)
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(Modifier.height(24.dp))
                NavigationDrawerItem(
                    label = { Text(stringResource(R.string.mi_perfil)) },
                    selected = false,
                    onClick = { navController.navigate("mi_perfil") }
                )
                NavigationDrawerItem(
                    label = { Text(stringResource(R.string.mis_mascotas)) },
                    selected = false,
                    onClick = { navController.navigate("lista_mis_mascotas") }
                )
                NavigationDrawerItem(
                    label = { Text(stringResource(R.string.cerrar_sesion)) },
                    selected = false,
                    onClick = {
                        navController.navigate("login") {
                            popUpTo("principal") { inclusive = true }
                        }
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(R.string.mascotas_sos)) },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = stringResource(R.string.menu))
                        }
                    }
                )
            },
            floatingActionButton = {
                if (selectedTab == 2) {
                    FloatingActionButton(
                        onClick = { navController.navigate("reporte_encontrada") }
                    ) { Text(stringResource(R.string.sos)) }
                }
            }
        ) { innerPadding ->
            Column(Modifier.padding(innerPadding)) {
                TabRow(selectedTabIndex = selectedTab) {
                    tabTitles.forEachIndexed { idx, title ->
                        Tab(
                            selected = selectedTab == idx,
                            onClick = { selectedTab = idx },
                            text = { Text(title) }
                        )
                    }
                }
                when (selectedTab) {
                    0 -> MascotaPerdidaListScreen(perdidas)
                    1 -> MascotaEncontradaListScreen(encontradas)
                    2 -> BotonesSOS(navController)
                }
            }
        }
    }
}

@Composable
private fun BotonesSOS(navController: NavController) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = { navController.navigate("mascota_perdida") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) { Text(stringResource(R.string.sos_reportar_perdida)) }

        Button(
            onClick = { navController.navigate("reporte_encontrada") },
            modifier = Modifier.fillMaxWidth()
        ) { Text(stringResource(R.string.encontre_mascota)) }
    }
}