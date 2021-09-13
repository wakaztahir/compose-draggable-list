package com.wakaztahir.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Icon
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.wakaztahir.draggablelist.DraggableList
import com.wakaztahir.draggablelist.draggableStateListOf
import com.wakaztahir.example.ui.theme.DraggableListTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DraggableListTheme {
                val personsList = remember {
                    draggableStateListOf(
                        Person("Shitty Person"),
                        Person("Good Person"),
                        Person("Not so Good Person"),
                        Person("Dick Person"),
                        Person("Fully Grown Asshole")
                    )
                }

                DraggableList(items = personsList) { item->
                    Row(Modifier.dragger()) {
                        TextField(
                            value = item.name,
                            onValueChange = {
                                item.name = it
                            }
                        )
                    }
                }
            }
        }
    }
}


class Person(name: String) {
    var name by mutableStateOf(name)
}