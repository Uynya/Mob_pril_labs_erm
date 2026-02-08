package com.example.kotlin1

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.kotlin1.ui.theme.Kotlin1Theme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import java.util.UUID

data class Task4(
    val id: String,
    val title: String,
    val description: String
)
class MainActivity4 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Kotlin1Theme {
                val list = remember { mutableStateListOf<Task4>() }.apply {
                    add(Task4("1", "Задача 1", "Выполнить лабораторную 6"))
                    add(Task4("2", "Задача 2", "Выполнить лабораторную 7"))
                }

                Scaffold(
                    floatingActionButton = { FloatingActionButton(list) },
                    topBar = { TopAppBar() },
                    modifier = Modifier.fillMaxSize(),
                    content = { innerPadding ->
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        ) {
                            Greeting2(
                                modifier = Modifier.weight(1f),
                                list = list
                            )
                            MyCustomTextField()
                        }
                    }
                )
                }
            }
        }
    }


@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar() {
    TopAppBar(
        title = { Text(text = "лялял люлюлю") },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        actions = {
            IconButton(onClick = { /* поиск, меню */ }) {
                Icon(Icons.Default.Search, contentDescription = null)
            }
        }
    )
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        style = MaterialTheme.typography.headlineLarge,
        text = "Hello $name!",
        modifier = modifier
    )
}
@Composable
fun Greeting2(modifier: Modifier = Modifier, list: MutableList<Task4>) {
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
fun FloatingActionButton(list: MutableList<Task4>) {
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
                                Task4(
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
fun MyCustomTextField() {
    var text by remember { mutableStateOf("") }
    OutlinedTextField(
        value = text,
        onValueChange = { newText: String ->
            text = newText },
        label = { Text("введите что-то") },
        placeholder = { Text("пиши пиши") },
        modifier = Modifier.fillMaxWidth()
    )
}





