package com.wakaztahir.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.wakaztahir.draggablelist.*
import com.wakaztahir.example.ui.theme.DraggableListTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DraggableListTheme {
                val foodList = remember {
                    draggableStateListOf(
                        Food("Apples"),
                        Food("Bananas"),
                        Food("Blueberries"),
                        Food("Oranges"),
                        Food("Watermelons")
                    )
                }

                Column {

                    // Non Lazy List
                    DraggableList(items = foodList) { item ->
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

                    Divider()

                    Text(
                        text = "Draggable List Using Lazy Column",
                        color = MaterialTheme.colors.onBackground
                    )

                    // Lazy List
                    val scope = rememberCoroutineScope()
                    var animationsEnabled by remember { mutableStateOf(true) }

                    LazyColumn {
                        draggableItems(
                            foodList,
                            scope = scope,
                            animationsEnabled = animationsEnabled,
                            updateAnimationsEnabled = {
                                animationsEnabled = it
                            }
                        ) { item ->
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
}


class Food(name: String) {
    var name by mutableStateOf(name)
}