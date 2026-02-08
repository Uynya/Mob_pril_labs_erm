package com.example.kotlin1.ui.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.kotlin1.Task

@Composable
fun HomeScreen(navController: NavHostController) {
//    val tasks = listOf(
//        Task("Купить продукты"),
//        Task("Сделать лабораторную"),
//        Task("Позвонить другу")
//    )

//    LazyColumn(
//        modifier = Modifier.fillMaxSize(),
//        contentPadding = PaddingValues(16.dp)
//    ) {
//        items(tasks) { task ->
//            Card(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(vertical = 4.dp)
//            ) {
//                Text(
//                    text = task.title,
//                    modifier = Modifier.padding(16.dp)
//                )
//            }
//        }
//    }
}