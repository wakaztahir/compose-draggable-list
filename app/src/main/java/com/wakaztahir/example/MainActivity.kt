package com.wakaztahir.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.wakaztahir.draggablelist.DraggableList
import com.wakaztahir.draggablelist.draggableItems
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

//                // Non Lazy List
//                DraggableList(items = personsList) { item ->
//                    Row(Modifier.dragger()) {
//                        OutlinedTextField(
//                            modifier = Modifier.fillMaxWidth(),
//                            value = item.name,
//                            onValueChange = {
//                                item.name = it
//                            },
//                            colors = TextFieldDefaults.outlinedTextFieldColors(textColor = MaterialTheme.colors.onBackground)
//                        )
//                    }
//                }

                // Lazy List
                val scope = rememberCoroutineScope()
                var animationsEnabled by remember { mutableStateOf(true) }

                LazyColumn {
                    draggableItems(
                        personsList,
                        scope = scope,
                        animationsEnabled = animationsEnabled,
                        updateAnimationsEnabled = {
                            animationsEnabled = it
                        }
                    ){ item->
                        Row(Modifier.dragger()) {
                            OutlinedTextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = item.name,
                                onValueChange = {
                                    item.name = it
                                },
                                colors = TextFieldDefaults.outlinedTextFieldColors(textColor = MaterialTheme.colors.onBackground)
                            )
                        }
                    }
                }
            }
        }
    }
}


class Person(name: String) {
    var name by mutableStateOf(name)
}