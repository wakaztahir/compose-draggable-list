package com.wakaztahir.draggablelist

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch

interface DraggableListScope {
    val columnScope: ColumnScope
    fun Modifier.dragger(): Modifier
}


@Composable
fun <T> DraggableList(
    modifier: Modifier = Modifier,
    items: SnapshotStateList<DraggableListItem<T>>,
    onReplace: (Int, Int) -> Unit = { index, newIndex ->
        val item = items[index]
        items.removeAt(index)
        items.add(newIndex, item)
    },
    content: @Composable DraggableListScope.(T) -> Unit,
) {
    val scope = rememberCoroutineScope()

    var animationsEnabled by remember { mutableStateOf(true) }

    Column(modifier = modifier) {
        items.forEachIndexed { index, item ->

            val topOffset by animateDpAsState(
                targetValue = item.topOffset,
                finishedListener = {
                    animationsEnabled = true
                }
            )
            var yOffset by remember { mutableStateOf(0.dp) }

            ListItem(
                modifier = Modifier.offset(y = yOffset + if (animationsEnabled) topOffset else item.topOffset),
                item = item,
                onVerticalDragged = {
                    var aboveHeight = 0.dp
                    items.subList(0, index)
                        .forEach { item -> aboveHeight -= item.itemHeight }
                    var belowHeight = 0.dp
                    items.subList(index, items.size - 1)
                        .forEach { item -> belowHeight += item.itemHeight }
                    if ((yOffset + it) > aboveHeight && (yOffset + it) < belowHeight) {
                        yOffset += it
                    }
                    scope.launch {
                        rearrangeItems(items, index, item, yOffset)
                    }
                },
                onVerticalDragStopped = {
                    scope.coroutineContext.cancelChildren()
                    scope.launch {
                        animationsEnabled = false
                        val newIndex = fixedResetItems(items, index, yOffset)
                        onReplace(index, newIndex)
                        yOffset = 0.dp
                    }
                },
                content = content
            )
        }
    }
}

