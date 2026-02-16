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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.unit.dp
import com.example.kotlin1.ui.theme.Kotlin1Theme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID
import kotlin.collections.plusAssign
import kotlin.random.Random

class lr10_2 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Kotlin1Theme {
                Column {
                    CoroutinesScreen()
                    FlowScreen()
                    StateFlowScreen()
                    SharedFlowScreen()
                    ErrorHandlingScreen()
                }
            }
        }
    }
}

// == первая часть ==
@Composable
fun CoroutinesScreen() {
    var isLoading by remember { mutableStateOf(false) }
    var result by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()
    Column {
        if (isLoading) {
            CircularProgressIndicator()
        }
        result?.let {
            Text(text = it)
        }
        Button(
            onClick = {
                isLoading = true
                result = null
                scope.launch {
                    val res = simulateLongOperation(2000)
                    result = res
                    isLoading = false
                }
            },
            enabled = !isLoading
        ) {
            Text("Запустить долгую операцию")
        }

        Button(
            onClick = {
                isLoading = true
                result = null
                scope.launch {
                    val numbers = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                    val sum = calculateSum(numbers)
                    result = "Сумма чисел: $sum"
                    isLoading = false
                }
            },
            enabled = !isLoading
        ) {
            Text("Вычислить сумму")
        }
    }
}
suspend fun calculateSum(numbers: List<Int>): Int {
    return withContext(Dispatchers.Default) {
        delay(1000)
        numbers.sum()
    }
}
suspend fun simulateLongOperation(duration: Long): String {
    delay(duration)
    return "Операция завершена за $duration мс"
}

// == вторая часть ==

fun numberFlow(): Flow<Int> = flow {
    for (i in 1..10) {
        delay(500)
        emit(i)
    }
}
fun transformedFlow(flow: Flow<Int>): Flow<Int> = flow
    .map { it * it }
    .filter { it % 2 == 0 }

fun errorFlow(): Flow<String> = flow {
    emit("Первое значение")
    delay(500)
    emit("Второе значение")
    delay(500)
    throw RuntimeException("Произошла ошибка!")
}.catch { exception ->
    emit("Ошибка обработана: ${exception.message}")
}
@Composable
fun FlowScreen() {
    var flowValues by remember { mutableStateOf<List<String>>(emptyList()) }
    val scope = rememberCoroutineScope()

    Column {
        LazyColumn {
            items(flowValues) { value ->
                Text(text = value)
            }
        }

        Button(
            onClick = {
                flowValues = emptyList()
                scope.launch {
                    numberFlow().collect { value ->
                        flowValues = flowValues + "Число: $value"
                    }
                }
            }
        ) {
            Text("Запустить Flow")
        }

        Button(
            onClick = {
                flowValues = emptyList()
                scope.launch {
                    transformedFlow(numberFlow()).collect { value ->
                        flowValues = flowValues + "Квадрат четного: $value"
                    }
                }
            }
        ) {
            Text("Запустить преобразованный Flow")
        }

        Button(
            onClick = {
                flowValues = emptyList()
                scope.launch {
                    errorFlow().collect { value ->
                        flowValues = flowValues + value
                    }
                }
            }
        ) {
            Text("Запустить Flow с ошибкой")
        }
    }
}

// == третья часть ==

@Composable
fun StateFlowScreen() {
    val counterStateFlow = remember { MutableStateFlow(0) }
    val counter: StateFlow<Int> = counterStateFlow.asStateFlow()
    val counterValue by counter.collectAsState()
    val isAutoIncrementingStateFlow = remember { MutableStateFlow(false) }
    val isAutoIncrementing: StateFlow<Boolean> = isAutoIncrementingStateFlow.asStateFlow()
    val isAutoIncrementingValue by isAutoIncrementing.collectAsState()
    val scope = rememberCoroutineScope()
    var autoIncrementJob by remember { mutableStateOf<Job?>(null) }

    fun increment() {
        counterStateFlow.value += 1
    }

    fun decrement() {
        counterStateFlow.value -= 1
    }

    fun reset() {
        counterStateFlow.value = 0
    }

    fun incrementBy(value: Int) {
        counterStateFlow.value += value
    }
    fun toggleAutoIncrement() {
        if (isAutoIncrementingValue) {
            isAutoIncrementingStateFlow.value = false
            autoIncrementJob?.cancel()
            autoIncrementJob = null
        } else {
            isAutoIncrementingStateFlow.value = true
            autoIncrementJob = scope.launch {
                while (true) {
                    delay(1000)
                    counterStateFlow.value += 1
                }
            }
        }
    }
    DisposableEffect(Unit) {
        onDispose {
            autoIncrementJob?.cancel()
        }
    }

    Column {
        Text(
            text = counterValue.toString(),
            style = MaterialTheme.typography.displayLarge
        )
        if (isAutoIncrementingValue) {
            Row {
                CircularProgressIndicator(modifier = Modifier.size(16.dp))
                Text("Автоинкремент активен")
            }
        }

        Row {
            Button(onClick = { decrement() }) { Text("-1") }
            Button(onClick = { increment() }) { Text("+1") }
        }

        Button(onClick = { reset() }) {
            Text("Сброс")
        }

        Button(onClick = { incrementBy(5) }) {
            Text("+5")
        }

        Button(onClick = { toggleAutoIncrement() }) {
            Text(if (isAutoIncrementingValue) "Остановить" else "Запустить")
        }
    }
}

// == четвёртая часть ==

@Composable
fun SharedFlowScreen() {
    val eventsSharedFlow = remember { MutableSharedFlow<String>(replay = 3) }
    val eventsFlow: SharedFlow<String> = eventsSharedFlow.asSharedFlow()
    var events by remember { mutableStateOf<List<String>>(emptyList()) }
    var eventCount by remember { mutableStateOf(0) }
    var eventCounter by remember { mutableStateOf(0) }
    val scope = rememberCoroutineScope()
    var isAutoGenerating by remember { mutableStateOf(false) }
    var autoGenerationJob by remember { mutableStateOf<Job?>(null) }
    LaunchedEffect(Unit) {
        eventsFlow.collect { event ->
            events = (events + event).takeLast(10) // Храним только последние 10
            eventCount++
        }
    }
    fun emitEvent(message: String) {
        scope.launch {
            eventsSharedFlow.emit(message)
        }
    }
    fun startAutoGeneration() {
        if (autoGenerationJob?.isActive == true) return
        isAutoGenerating = true
        autoGenerationJob = scope.launch {
            while (true) {
                delay(2000)
                eventCounter++
                val randomNumber = Random.nextInt(1, 101)
                emitEvent("Событие #$eventCounter: $randomNumber")
            }
        }
    }
    fun stopAutoGeneration() {
        isAutoGenerating = false
        autoGenerationJob?.cancel()
        autoGenerationJob = null
    }
    DisposableEffect(Unit) {
        onDispose {
            autoGenerationJob?.cancel()
        }
    }
    Column {
        Text("Всего событий: $eventCount")
        LazyColumn {
            items(events.reversed()) { event ->
                Card {
                    Text(text = event)
                }
            }
        }
        Button(
            onClick = {
                emitEvent("Ручное событие #${eventCount + 1}")
            }
        ) {
            Text("Сгенерировать событие")
        }
        Button(
            onClick = {
                if (isAutoGenerating) {
                    stopAutoGeneration()
                } else {
                    startAutoGeneration()
                }
            }
        ) {
            Text(if (isAutoGenerating) "Остановить" else "Запустить")
        }
    }
}

// == пятая часть ==

suspend fun riskyOperation(success: Boolean): String {
    delay(1000)
    if (!success) {
        throw IllegalStateException("Операция не удалась")
    }
    return "Операция выполнена успешно"
}

fun riskyFlow(): Flow<String> = flow {
    emit("Шаг 1")
    delay(500)
    emit("Шаг 2")
    delay(500)
    throw RuntimeException("Ошибка на шаге 3!")
    emit("Шаг 3")
}.catch { exception ->
    emit("Ошибка обработана: ${exception.message}")
}

suspend fun safeOperation(success: Boolean): Result<String> {
    return try {
        delay(1000)
        if (!success) {
            Result.failure(IllegalStateException("Операция не удалась"))
        } else {
            Result.success("Операция выполнена успешно")
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}
@Composable
fun ErrorHandlingScreen() {
    var result by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()
    Column {
        result?.let {
            Card {
                Text(text = it)
            }
        }
        errorMessage?.let {
            Card(colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            )) {
                Text("Ошибка: $it")
            }
        }
        Button(
            onClick = {
                result = null
                errorMessage = null
                scope.launch {
                    try {
                        val res = riskyOperation(true)
                        result = res
                    } catch (e: Exception) {
                        errorMessage = e.message
                    }
                }
            }
        ) {
            Text("Успешная операция")
        }
        Button(
            onClick = {
                result = null
                errorMessage = null
                scope.launch {
                    try {
                        val res = riskyOperation(false)
                        result = res
                    } catch (e: Exception) {
                        errorMessage = e.message
                    }
                }
            }
        ) {
            Text("Операция с ошибкой")
        }
        Button(
            onClick = {
                result = null
                errorMessage = null
                scope.launch {
                    riskyFlow().collect { value ->
                        result = value
                        delay(500)
                    }
                }
            }
        ) {
            Text("Flow с обработкой ошибок")
        }
        Button(
            onClick = {
                result = null
                errorMessage = null
                scope.launch {
                    val safeResult = safeOperation(false)
                    safeResult.fold(
                        onSuccess = { result = it },
                        onFailure = { errorMessage = it.message ?: "Неизвестная ошибка" }
                    )
                }
            }
        ) {
            Text("Безопасная операция")
        }
    }
}








