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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
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

// SRP: Класс отвечает только за хранение данных
interface Task {
    val id: String
    val title: String
    val description: String
}

// LSP: Реализация не нарушает контракт интерфейса
data class BasicTask(
    override val id: String,
    override val title: String,
    override val description: String
) : Task

// ISP: Узкие интерфейсы для конкретных операций
interface TaskReader {
    fun getAllTasks(): List<Task>
}

interface TaskWriter {
    fun addTask(task: Task)
}

interface TaskObserver {
    fun observeTasks(): List<Task>
}

// DIP: Зависимость от абстракций
interface TaskRepository : TaskReader, TaskWriter, TaskObserver


// SRP: Класс отвечает только за управление данными
class InMemoryTaskRepository : TaskRepository {
    private val _tasks = mutableStateListOf<Task>()

    override fun getAllTasks(): List<Task> = _tasks.toList()

    override fun addTask(task: Task) {
        _tasks.add(task)
    }

    override fun observeTasks(): List<Task> = _tasks.toList()
}

// OCP: Новые стратегии добавляются без изменения существующего кода
interface LayoutStrategy {
    @Composable
    fun CreateLayout(
        modifier: Modifier,
        tasks: List<Task>,
        contentPadding: PaddingValues,
        cardPadding: androidx.compose.ui.unit.Dp,
        onTaskClick: (Task) -> Unit
    )
}

class CompactLayoutStrategy : LayoutStrategy {
    @Composable
    override fun CreateLayout(
        modifier: Modifier,
        tasks: List<Task>,
        contentPadding: PaddingValues,
        cardPadding: androidx.compose.ui.unit.Dp,
        onTaskClick: (Task) -> Unit
    ) {
        LazyColumn(
            modifier = modifier,
            contentPadding = contentPadding,
            verticalArrangement = Arrangement.spacedBy(cardPadding)
        ) {
            items(tasks) { task ->
                TaskCard(
                    task = task,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onTaskClick(task) }
                )
            }
        }
    }
}

class MediumLayoutStrategy : LayoutStrategy {
    @Composable
    override fun CreateLayout(
        modifier: Modifier,
        tasks: List<Task>,
        contentPadding: PaddingValues,
        cardPadding: androidx.compose.ui.unit.Dp,
        onTaskClick: (Task) -> Unit
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = modifier,
            contentPadding = contentPadding,
            horizontalArrangement = Arrangement.spacedBy(cardPadding),
            verticalArrangement = Arrangement.spacedBy(cardPadding)
        ) {
            items(tasks) { task ->
                TaskCard(
                    task = task,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onTaskClick(task) }
                )
            }
        }
    }
}

class ExpandedLayoutStrategy : LayoutStrategy {
    @Composable
    override fun CreateLayout(
        modifier: Modifier,
        tasks: List<Task>,
        contentPadding: PaddingValues,
        cardPadding: androidx.compose.ui.unit.Dp,
        onTaskClick: (Task) -> Unit
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = modifier,
            contentPadding = contentPadding,
            horizontalArrangement = Arrangement.spacedBy(cardPadding),
            verticalArrangement = Arrangement.spacedBy(cardPadding)
        ) {
            items(tasks) { task ->
                TaskCard(
                    task = task,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onTaskClick(task) }
                )
            }
        }
    }
}

// OCP: Новая стратегия добавляется без изменения фабрики
class LayoutStrategyFactory {
    fun getStrategy(windowSizeClass: WindowSizeClass): LayoutStrategy = when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> CompactLayoutStrategy()
        WindowWidthSizeClass.Medium -> MediumLayoutStrategy()
        WindowWidthSizeClass.Expanded -> ExpandedLayoutStrategy()
        else -> CompactLayoutStrategy()
    }
}

// SRP: Компонент отвечает только за отображение карточки
@Composable
fun TaskCard(
    task: Task,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = onClick
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                text = task.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = task.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// SRP: Класс отвечает только за управление состоянием экрана
// DIP: Зависит от абстракции TaskRepository
class TasksViewModel(
    private val repository: TaskRepository
) {
    private val _tasks = mutableStateListOf<Task>()
    val tasks: List<Task> = _tasks

    init {
        refreshTasks()
    }

    fun refreshTasks() {
        _tasks.clear()
        _tasks.addAll(repository.observeTasks())
    }

    fun addTask(title: String, description: String) {
        if (title.isNotBlank()) {
            val task = BasicTask(
                id = UUID.randomUUID().toString(),
                title = title.trim(),
                description = description.trim()
            )
            repository.addTask(task)
            refreshTasks()
        }
    }

    fun onTaskClick(task: Task) {
        // Обработка клика по задаче
    }
}

@Composable
fun NotesScreen(
    modifier: Modifier = Modifier,
    viewModel: TasksViewModel,
    windowSizeClass: WindowSizeClass
) {
    val layoutFactory = remember { LayoutStrategyFactory() }
    val strategy = remember(windowSizeClass) { layoutFactory.getStrategy(windowSizeClass) }

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

    val contentPadding = PaddingValues(
        horizontal = horizontalPadding,
        vertical = cardPadding
    )

    strategy.CreateLayout(
        modifier = modifier,
        tasks = viewModel.tasks,
        contentPadding = contentPadding,
        cardPadding = cardPadding,
        onTaskClick = viewModel::onTaskClick
    )
}

@Composable
fun AddTaskDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Новая заметка",
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
                            text = "Заголовок заметки",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    placeholder = {
                        Text(
                            text = "Введите заголовок",
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
                            text = "Описание заметки",
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
                    onConfirm(title, description)
                    onDismiss()
                },
                modifier = Modifier.minimumInteractiveComponentSize()
            ) {
                Text(text = "Сохранить", style = MaterialTheme.typography.bodyMedium)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.minimumInteractiveComponentSize()
            ) {
                Text(text = "Отмена", style = MaterialTheme.typography.bodyMedium)
            }
        }
    )
}

@Composable
fun FloatingActionButton5(
    onAddClick: () -> Unit
) {
    FloatingActionButton(
        onClick = onAddClick,
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        modifier = Modifier.minimumInteractiveComponentSize(),
        content = {
            Icon(Icons.Default.Add, contentDescription = "Добавить новую заметку")
        }
    )
}

interface ThemeManager {
    fun getThemeMode(): ThemeMode
    fun setThemeMode(mode: ThemeMode)
}

class ThemeManagerImpl : ThemeManager {
    private var currentThemeMode: ThemeMode = ThemeMode.System

    override fun getThemeMode(): ThemeMode = currentThemeMode

    override fun setThemeMode(mode: ThemeMode) {
        currentThemeMode = mode
    }
}

@Composable
fun MyApplicationTheme(
    themeManager: ThemeManager,
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val darkTheme = when (themeManager.getThemeMode()) {
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
        typography = MaterialTheme.typography,
        content = content
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    themeManager: ThemeManager,
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
                    val newMode = when (themeManager.getThemeMode()) {
                        ThemeMode.System -> ThemeMode.Light
                        ThemeMode.Light -> ThemeMode.Dark
                        ThemeMode.Dark -> ThemeMode.System
                    }
                    themeManager.setThemeMode(newMode)
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

class labs7_8 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // DIP: Создание зависимостей на верхнем уровне
            val repository = remember { InMemoryTaskRepository() }
            val viewModel = remember { TasksViewModel(repository) }
            val themeManager = remember { ThemeManagerImpl() }

            var showDialog by remember { mutableStateOf(false) }

            @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
            val windowSizeClass = calculateWindowSizeClass(this)

            MyApplicationTheme(themeManager = themeManager) {
                Scaffold(
                    floatingActionButton = {
                        FloatingActionButton5(
                            onAddClick = { showDialog = true }
                        )
                    },
                    topBar = {
                        TopAppBar(
                            themeManager = themeManager,
                            windowSizeClass = windowSizeClass
                        )
                    },
                    modifier = Modifier.fillMaxSize(),
                ) { innerPadding ->
                    NotesScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        viewModel = viewModel,
                        windowSizeClass = windowSizeClass
                    )
                }

                if (showDialog) {
                    AddTaskDialog(
                        onDismiss = { showDialog = false },
                        onConfirm = { title, description ->
                            viewModel.addTask(title, description)
                            showDialog = false
                        }
                    )
                }
            }
        }
    }
}