package com.example.kotlin1

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.kotlin1.ui.theme.Kotlin1Theme
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import java.util.UUID

enum class ThemeMode {
    Light,
    Dark,
    System
}

data class Task5(
    val id: String,
    val title: String,
    val description: String
)

class MainActivity5 : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val list = remember { mutableStateListOf<Task5>() }
            var themeMode by remember { mutableStateOf(ThemeMode.System) }
            MyApplicationTheme(themeMode = themeMode) {
                Scaffold(
                    floatingActionButton = {FloatingActionButton5(list)},
                    topBar = {
                        TopAppBar(
                            themeMode = themeMode,
                            onThemeModeChange = { themeMode = it }
                        )
                    },
                    modifier = Modifier.fillMaxSize(),

                ) { innerPadding ->
                    Greeting5(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        list
                    )
                }

            }
        }
    }
}


@Composable
fun Greeting5(modifier: Modifier = Modifier, list: MutableList<Task5>) {
    LazyColumn(modifier = modifier) {
        items(list) { item ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(item.title, style = MaterialTheme.typography.titleMedium)
                    Text(item.description, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}

@Composable
fun FloatingActionButton5(list: MutableList<Task5>) {
    var showDialog by remember { mutableStateOf(false) }

    FloatingActionButton(
        onClick = { showDialog = true },
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        content = {
            Icon(Icons.Default.Add, contentDescription = "тут текстик должен быть")
        }
    )
    if (showDialog) {
        var title by remember { mutableStateOf("") }
        var description by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Новая заметка") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Новая заметка")},
                        placeholder = { Text("Текст заметки") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Описание заметки") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (title.isNotBlank()) {
                            list.add(
                                Task5(
                                    id = UUID.randomUUID().toString(),
                                    title = title.trim(),
                                    description = description.trim()
                                )
                            )
                            title = ""
                            description = ""
                            showDialog = false
                        }
                    }
                ) {
                    Text("Сохранить")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Отмена")
                }
            }
        )
    }
}

@Composable
fun MyApplicationTheme(
    themeMode: ThemeMode = ThemeMode.System,

    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val darkTheme = when (themeMode) {
        ThemeMode.Light -> false
        ThemeMode.Dark -> true
        ThemeMode.System -> isSystemInDarkTheme()
    }
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }


        darkTheme -> darkColorScheme()
        else -> lightColorScheme()
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        content = content
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    themeMode: ThemeMode,
    onThemeModeChange: (ThemeMode) -> Unit
) {
    TopAppBar(
        title = { Text(stringResource(R.string.notes_title)) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        actions = {
            IconButton(onClick = { /* поиск, меню */ }) {
                Icon(Icons.Default.Search, contentDescription = null)
            }
            IconButton(onClick = {
                val newMode = when (themeMode) {
                    ThemeMode.System -> ThemeMode.Light
                    ThemeMode.Light -> ThemeMode.Dark
                    ThemeMode.Dark -> ThemeMode.System
                }
                onThemeModeChange(newMode)
            }) {
                Icon(
                    imageVector = Icons.Default.Bedtime,
                    contentDescription = "Переключить тему"
                )
            }
        }
    )
}



