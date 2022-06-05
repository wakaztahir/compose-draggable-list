package com.wakaztahir.common

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Measured
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch

interface DraggableListScope : ColumnScope {
    fun Modifier.dragger(): Modifier
}


@Composable
fun <T> DraggableListIndexed(
    modifier: Modifier = Modifier,
    items: SnapshotStateList<DraggableListItem<T>>,
    onReplace: (Int, Int) -> Unit = { index, newIndex ->
        val item = items[index]
        items.removeAt(index)
        items.add(newIndex, item)
    },
    content: @Composable DraggableListScope.(Int, T) -> Unit,
) {
    val scope = rememberCoroutineScope()

    val density = LocalDensity.current

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

            val scope = remember(item) {
                object : DraggableListScope {
                    override fun Modifier.align(alignment: Alignment.Horizontal): Modifier =
                        this@Column.run { Modifier.align(alignment) }

                    override fun Modifier.alignBy(alignmentLineBlock: (Measured) -> Int): Modifier =
                        this@Column.run { Modifier.alignBy(alignmentLineBlock) }

                    override fun Modifier.alignBy(alignmentLine: VerticalAlignmentLine): Modifier =
                        this@Column.run { Modifier.alignBy(alignmentLine) }

                    override fun Modifier.weight(weight: Float, fill: Boolean): Modifier =
                        this@Column.run { Modifier.weight(weight, fill) }

                    override fun Modifier.dragger(): Modifier {
                        return this.draggable(
                            verticalDraggableState,
                            Orientation.Vertical,
                            onDragStopped = {
                                scope.coroutineContext.cancelChildren()
                                scope.launch {
                                    animationsEnabled = false
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
                    }
            ) {
                content(scope, index, item.item)
            }
        }
    }
}

