package com.wakaztahir.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import com.wakaztahir.draggablelist.draggableItemsIndexed
import com.wakaztahir.draggablelist.draggableStateListOf

@Composable
fun App() {
    var list = draggableStateListOf("sdf", "sdf", "sdf", "sdfsdfasdf", "dslkfjsalkdfj", "slkdjflkds")

    var animationsEnabled by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    LazyColumn {
        draggableItemsIndexed(
            list,
            animationsEnabled = animationsEnabled,
            updateAnimationsEnabled = {
                animationsEnabled = it
            },
            scope = scope
        ) { index, item ->
            Text(
                modifier = Modifier.dragger(),
                text = "$index $item"
            )
        }
    }
}
