package com.proyecto.appmascotassos4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.navigation.compose.rememberNavController
import com.proyecto.appmascotassos4.ui.navigation.AppNavigation
import com.proyecto.appmascotassos4.ui.theme.AppMascotasSOS4Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppMascotasSOS4Theme {
                Surface {
                    val navController = rememberNavController()
                    AppNavigation(navController = navController)
                }
            }
        }
    }
}