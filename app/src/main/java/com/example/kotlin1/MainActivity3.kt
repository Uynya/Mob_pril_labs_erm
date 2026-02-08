package com.example.kotlin1

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.kotlin1.ui.theme.Kotlin1Theme
import androidx.compose.ui.platform.LocalContext

class MainActivity3 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Kotlin1Theme {
                StyledCard_prev()
            }
        }
    }
}

@Composable
fun GreetingScreen() {
    Column (verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Привет, Compose!")
        Button(onClick = { /* пока пусто */ }) {
            Text("Нажми")
        }
        Text("текст ради текста")
        Button(onClick = { /* пока пусто */ }) {
            Text("кнопка ради кнопки")
        }
        StatsRow()
        OverlayBadge()
    }
}
@Composable
fun StatsRow(){
    Row( horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()){
        Text("Лекций: 19")
        Text("Практик: 31")
        Text("Курс: 3")
    }
}
@Composable
fun OverlayBadge(){
    Box(modifier = Modifier.background(Color.LightGray),
        contentAlignment = Alignment.Center){
        Text("Текстик")
        Text(modifier = Modifier.align(Alignment.TopEnd), text = "NEW" )
    }
}
@Composable
fun AboutScreen(){
    Column(horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "О приложении")
        Text(text = "описание")
        Row(){
            Button(onClick = { /* пока пусто */ }) {
                Text("версия")
            }
            Button(onClick = { /* пока пусто */ }) {
                Text("лицензия")
            }
        }
    }
}


@Preview(name = "Светлая тема", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO, device = Devices.PIXEL_4)
@Composable
fun AboutScreenPreview_2(){
    AboutScreen()
}
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Тёмная тема", device = Devices.NEXUS_9)
@Composable
fun AboutScreenPreview_3(){
    AboutScreen()
}

@Composable()
fun StyledCard(){
    val context = LocalContext.current
    Column (Modifier.fillMaxWidth().background(Color.LightGray,  RoundedCornerShape(12.dp)).padding(16.dp).clickable{Toast.makeText(context, "Нажато",
        Toast.LENGTH_SHORT).show()},
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement =
        Arrangement.spacedBy(8.dp)) {
        Text("Заголовочек")
        Text("Подзаголовочек")
        Button(onClick = { /* пока пусто */ }) {
            Text("кнопка ради кнопки")
        }
    }
}
@Preview
@Composable
fun StyledCard_prev(){
    StyledCard()
}
