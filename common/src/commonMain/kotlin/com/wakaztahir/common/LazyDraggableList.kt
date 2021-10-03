package com.wakaztahir.draggablelist

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

interface LazyDraggableListScope {
    fun Modifier.dragger(): Modifier
}

fun <T> LazyListScope.draggableItemsIndexed(
    items: SnapshotStateList<DraggableListItem<T>>,
    scope: CoroutineScope,
    animationsEnabled: Boolean,
    updateAnimationsEnabled: (Boolean) -> Unit,
    itemContent: @Composable LazyDraggableListScope.(Int, T) -> Unit
) {
    itemsIndexed(items) { index, item ->

        var yOffset by remember { mutableStateOf(0.dp) }

        val topOffset by animateDpAsState(
            targetValue = item.topOffset,
            finishedListener = {
                updateAnimationsEnabled(true)
            }
        )

        val density = LocalDensity.current

        val verticalDraggableState = rememberDraggableState {
            val drag = with(density) { it.toDp() }
            var aboveHeight = 0.dp
            items.subList(0, index)
                .forEach { item -> aboveHeight -= item.itemHeight }
            var belowHeight = 0.dp
            items.subList(index, items.size - 1)
                .forEach { item -> belowHeight += item.itemHeight }
            if ((yOffset + drag) > aboveHeight && (yOffset + drag) < belowHeight) {
                yOffset += drag
            }
            scope.launch {
                rearrangeItems(items, index, item, yOffset)
            }
        }

        val onReplace: (Int, Int) -> Unit = { index, newIndex ->
            val item = items[index]
            items.removeAt(index)
            items.add(newIndex, item)
        }

        val scope = remember {
            object : LazyDraggableListScope {
                override fun Modifier.dragger(): Modifier {
                    return this.draggable(
                        verticalDraggableState,
                        Orientation.Vertical,
                        onDragStopped = {
                            scope.launch {
                                updateAnimationsEnabled(false)
                                val newIndex = fixedResetItems(items, index, yOffset)
                                onReplace(index, newIndex)
                                yOffset = 0.dp
                            }
                        }
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .offset(y = yOffset + if (animationsEnabled) topOffset else item.topOffset)
                .onSizeChanged {
                    item.itemHeight = with(density) {
                        it.height.toDp()
                    }
                }) {
            itemContent(scope, index, item.item)
        }
    }
}