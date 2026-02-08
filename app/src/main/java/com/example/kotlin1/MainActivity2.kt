package com.example.kotlin1

import android.R
import android.R.color.white
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFrom
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.kotlin1.ui.theme.Kotlin1Theme
import com.example.kotlin1.Task

class MainActivity2 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Kotlin1Theme {
                HomeScreenPreview()
            }
        }
    }
}

@Composable
fun HomeScreen(list: List<Task>){
//    val tasks = listOf(Task(id = 1, title = "раз"), Task(id = 2, title = "два"), Task(id = 3, title = "три")
//    )
//
//    LazyColumn {
//        items(tasks){ item ->
//            Text(item.title)
//        }
//    }

    LazyColumn (modifier = Modifier.padding(4.dp)
        .background(color = Color.Yellow)
        ){
        items(list){
            item -> Text(item.title)
        }
    }
}
@Preview
@Composable
fun HomeScreenPreview(){

    val tasks = listOf(Task(id = 1, title = "юра"), Task(id = 2, title = "антон"), Task(id = 3, title = "а влада нет")
   )
    Column {
        Text(text = "Список:")
        HomeScreen(tasks)
    }

}
