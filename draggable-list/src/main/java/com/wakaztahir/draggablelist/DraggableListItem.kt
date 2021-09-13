package com.wakaztahir.draggablelist

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope


class DraggableListItem<T>(val item: T) {
    var topOffset by mutableStateOf(0.dp)
    var itemHeight by mutableStateOf(0.dp)
}

@Composable
internal fun <T> ColumnScope.ListItem(
    modifier: Modifier = Modifier,
    item: DraggableListItem<T>,
    onVerticalDragged: (Dp) -> Unit,
    onVerticalDragStopped: suspend CoroutineScope.(Float) -> Unit,
    content: @Composable DraggableListScope.(T) -> Unit,
) {

    val density = LocalDensity.current

    val verticalDraggableState = rememberDraggableState {
        onVerticalDragged(with(density) { it.toDp() })
    }

    val scope = object : DraggableListScope {
        override val columnScope: ColumnScope = this@ListItem

        override fun Modifier.dragger(): Modifier {
            return this.draggable(
                verticalDraggableState,
                Orientation.Vertical,
                onDragStopped = onVerticalDragStopped
            )
        }
    }

    Box(modifier = modifier.onSizeChanged {
        item.itemHeight = with(density) {
            it.height.toDp()
        }
    }) {
        content(scope, item.item)
    }
}