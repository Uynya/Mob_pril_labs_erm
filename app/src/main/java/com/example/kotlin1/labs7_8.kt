package com.example.kotlin1

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.ui.unit.dp
import java.util.UUID

enum class ThemeMode {
    Light,
    Dark,
    System
}

data class Task5( //почему таск5?
// до этого файл был mainActivity5 и при использовании класса task он ссылался на другой класс в другом файле :Р
    val id: String,
    val title: String,
    val description: String
)

class labs7_8 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val list = remember { mutableStateListOf<Task5>() }
            var themeMode by remember { mutableStateOf(ThemeMode.System) }

            @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
            val windowSizeClass = calculateWindowSizeClass(this)

            MyApplicationTheme(themeMode = themeMode) {
                Scaffold(
                    floatingActionButton = { FloatingActionButton5(list) },
                    topBar = {
                        TopAppBar(
                            themeMode = themeMode,
                            onThemeModeChange = { themeMode = it },
                            windowSizeClass = windowSizeClass
                        )
                    },
                    modifier = Modifier.fillMaxSize(),
                ) { innerPadding ->
                    NotesScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        list = list,
                        windowSizeClass = windowSizeClass
                    )
                }
            }
        }
    }
}

@Composable
fun NotesScreen(
    modifier: Modifier = Modifier,
    list: MutableList<Task5>,
    windowSizeClass: WindowSizeClass
) {
    val horizontalPadding = when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> 8.dp
        WindowWidthSizeClass.Medium -> 16.dp
        WindowWidthSizeClass.Expanded -> 24.dp
        else -> 8.dp
    }

    val cardPadding = when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> 8.dp
        WindowWidthSizeClass.Medium -> 12.dp
        WindowWidthSizeClass.Expanded -> 16.dp
        else -> 8.dp
    }

    val contentPadding = when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> 16.dp
        WindowWidthSizeClass.Medium -> 20.dp
        WindowWidthSizeClass.Expanded -> 24.dp
        else -> 16.dp
    }

    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Expanded -> {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = modifier,
                contentPadding = PaddingValues(horizontalPadding),
                horizontalArrangement = Arrangement.spacedBy(cardPadding),
                verticalArrangement = Arrangement.spacedBy(cardPadding)
            ) {
                items(list) { item ->
                    TaskCard(
                        item = item,
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = contentPadding
                    )
                }
            }
        }

        WindowWidthSizeClass.Medium -> {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = modifier,
                contentPadding = PaddingValues(horizontalPadding),
                horizontalArrangement = Arrangement.spacedBy(cardPadding),
                verticalArrangement = Arrangement.spacedBy(cardPadding)
            ) {
                items(list) { item ->
                    TaskCard(
                        item = item,
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = contentPadding
                    )
                }
            }
        }

        else -> {
            LazyColumn(
                modifier = modifier,
                contentPadding = PaddingValues(
                    horizontal = horizontalPadding,
                    vertical = cardPadding
                ),
                verticalArrangement = Arrangement.spacedBy(cardPadding)
            ) {
                items(list) { item ->
                    TaskCard(
                        item = item,
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = contentPadding
                    )
                }
            }
        }
    }
}

@Composable
fun TaskCard(
    item: Task5,
    modifier: Modifier = Modifier,
    contentPadding: androidx.compose.ui.unit.Dp = 16.dp
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(Modifier.padding(contentPadding)) {
            Text(
                item.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                item.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
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
        modifier = Modifier.minimumInteractiveComponentSize(),
        content = {
            Icon(Icons.Default.Add, contentDescription = "Добавить новую заметку")
        }
    )
    if (showDialog) {
        var title by remember { mutableStateOf("") }
        var description by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Text(
                    "Новая заметка",
                    style = MaterialTheme.typography.headlineMedium
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = {
                            Text(
                                "Заголовок заметки",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        placeholder = {
                            Text(
                                "Введите заголовок",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = {
                            Text(
                                "Описание заметки",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
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
                    },
                    modifier = Modifier.minimumInteractiveComponentSize()
                ) {
                    Text("Сохранить", style = MaterialTheme.typography.bodyMedium)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDialog = false },
                    modifier = Modifier.minimumInteractiveComponentSize()
                ) {
                    Text("Отмена", style = MaterialTheme.typography.bodyMedium)
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
    onThemeModeChange: (ThemeMode) -> Unit,
    windowSizeClass: WindowSizeClass
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE

    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.notes_title),
                style = when {
                    isLandscape && windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded ->
                        MaterialTheme.typography.headlineLarge
                    windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded ->
                        MaterialTheme.typography.headlineMedium
                    windowSizeClass.widthSizeClass == WindowWidthSizeClass.Medium ->
                        MaterialTheme.typography.titleLarge
                    else -> MaterialTheme.typography.titleMedium
                },
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        actions = {
            IconButton(
                onClick = { /* поиск, меню */ },
                modifier = Modifier.minimumInteractiveComponentSize()
            ) {
                Icon(Icons.Default.Search, contentDescription = "Поиск заметок")
            }
            IconButton(
                onClick = {
                    val newMode = when (themeMode) {
                        ThemeMode.System -> ThemeMode.Light
                        ThemeMode.Light -> ThemeMode.Dark
                        ThemeMode.Dark -> ThemeMode.System
                    }
                    onThemeModeChange(newMode)
                },
                modifier = Modifier.minimumInteractiveComponentSize()
            ) {
                Icon(
                    imageVector = Icons.Default.Bedtime,
                    contentDescription = "Переключить тему"
                )
            }
        }
    )
}