package com.example.kotlin1


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.kotlin1.data.NavItem
import com.example.kotlin1.ui.screens.AddTaskScreen
import com.example.kotlin1.ui.screens.HomeScreen
import com.example.kotlin1.ui.screens.ProfileScreen
import com.example.kotlin1.ui.screens.WeatherScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
        }
    }
}

//data class Task(val title: String)
data class User(val name: String, val email: String)
data class Weather(val city: String, val temperature: String)

object Screens {
    const val MAIN = "main"
    const val WEATHER = "weather"
    const val ADD_TASK = "addTask"
    const val PROFILE = "profile"

    val bottomNavItems = listOf(
        NavItem(MAIN, "Главная", Icons.Default.Home),
        NavItem(WEATHER, "Погода", Icons.Default.WbSunny),
        NavItem(PROFILE, "Профиль", Icons.Default.Person)
    )
}

@Composable
fun MyApp() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) },
        floatingActionButton = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            if (currentRoute == Screens.MAIN) {
                FloatingActionButton(onClick = { navController.navigate("addTask") }) {
                    Icon(Icons.Default.Add, contentDescription = "Добавить задачу")
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screens.MAIN,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screens.MAIN) { HomeScreen(navController) }
            composable(Screens.WEATHER) { WeatherScreen() }
            composable(Screens.PROFILE) { ProfileScreen() }
            composable(Screens.ADD_TASK) { AddTaskScreen(navController) }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        Screens.bottomNavItems.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = { navController.navigate(item.route) },
                icon = { Icon(item.icon, item.label) },
                label = { Text(item.label) }
            )
        }
    }
}