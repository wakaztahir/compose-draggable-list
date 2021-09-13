package com.wakaztahir.draggablelist

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch

interface DraggableListScope {
    val columnScope: ColumnScope
    fun Modifier.dragger() : Modifier
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

    /**
     * Rearranges other items as one item is being dragged
     */
    val rearrangeItems: suspend (Int, DraggableListItem<T>, Dp) -> Unit = { index, item, yOffset ->

        val tolerance = 5.dp

        if (yOffset < 0.dp) { // item is going up
            var itemHeights = 0.dp
            items.subList(0, index).reversed().forEach { each ->
                itemHeights += each.itemHeight
                if (itemHeights < (-yOffset + tolerance)) {
                    each.topOffset = item.itemHeight
                } else {
                    each.topOffset = 0.dp
                }
            }
        } else { // item is going down
            var itemHeights = 0.dp
            items.subList(index + 1, items.size).forEach { each ->
                itemHeights += each.itemHeight
                if ((yOffset + tolerance) > itemHeights) {
                    each.topOffset = -item.itemHeight
                } else {
                    each.topOffset = 0.dp
                }
            }
        }
    }

    /**
     * Resets the items after they have got new arrangement
     */
    val fixedResetOffset: suspend (Dp, Int, DraggableListItem<T>) -> Unit =
        { yOffset, index, item ->
            animationsEnabled = false
            var newIndex = index
            var itemHeights = 0.dp
            val tolerance = 5.dp
            if (yOffset > 0.dp) { // item is below its current position
                items.subList(index + 1, items.size).forEach { each ->
                    itemHeights += each.itemHeight
                    if ((yOffset + tolerance) > itemHeights) {
                        newIndex++
                    }
                }
            } else { // item is above its current position
                items.subList(0, index).reversed().forEach { each ->
                    itemHeights += each.itemHeight
                    if (itemHeights < (-yOffset + tolerance)) {
                        newIndex--
                    }
                }
            }

            //Resetting offsets
            items.forEach { each ->
                each.topOffset = 0.dp
            }

            //Changing Indexes of Items
            onReplace(index, newIndex)
        }

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
                        rearrangeItems(index, item, yOffset)
                    }
                },
                onVerticalDragStopped = {
                    scope.coroutineContext.cancelChildren()
                    scope.launch {
                        fixedResetOffset(yOffset, index, item)
                        yOffset = 0.dp
                    }
                },
                content = content
            )
        }
    }
}

