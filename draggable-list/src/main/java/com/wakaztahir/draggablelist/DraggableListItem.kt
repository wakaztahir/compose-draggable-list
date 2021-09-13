package com.wakaztahir.draggablelist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp

class DraggableListItem<T>(val item: T) {
    var topOffset by mutableStateOf(0.dp)
    var itemHeight by mutableStateOf(0.dp)
}