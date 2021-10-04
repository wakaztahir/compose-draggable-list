package com.wakaztahir.common

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import kotlinx.coroutines.CoroutineScope

/**
 * Provides a mutable state list that can be used with [DraggableList] ,
 * [DraggableListIndexed] or [draggableItems] and [draggableItemsIndexed]
 */
fun <T> draggableStateListOf(vararg elements: T): SnapshotStateList<DraggableListItem<T>> =
    mutableStateListOf(
        *elements.map { DraggableListItem(it) }.toTypedArray()
    )

/**
 * Provides a mutable state list that can be used with [DraggableList] ,
 * [DraggableListIndexed] or [draggableItems] and [draggableItemsIndexed]
 */
fun <T> draggableStateListOf(): SnapshotStateList<DraggableListItem<T>> =
    mutableStateListOf()

/**
 * Makes your items draggable in a non-lazy list ,
 * You can render your item in [content] , if index is required use [DraggableListIndexed] ,
 * [items] can be made using [draggableStateListOf] ,
 * Here's a lazy list implementation : [draggableItems]
 */
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
) = DraggableListIndexed(
    modifier = modifier,
    items = items,
    onReplace = onReplace,
    content = { index, item ->
        content(item)
    }
)


/**
 * Makes your items draggable in lazy list
 * if index is required , use [draggableItemsIndexed] ,
 * [items] can be made using [draggableStateListOf] ,
 * Use [DraggableList] for a non lazy implementation
 */
fun <T> LazyListScope.draggableItems(
    items: SnapshotStateList<DraggableListItem<T>>,
    scope: CoroutineScope,
    animationsEnabled: Boolean,
    updateAnimationsEnabled: (Boolean) -> Unit,
    itemContent: @Composable LazyDraggableItemScope.(T) -> Unit
) = draggableItemsIndexed(
    items = items,
    scope = scope,
    animationsEnabled = animationsEnabled,
    updateAnimationsEnabled = updateAnimationsEnabled,
    itemContent = { index, item ->
        itemContent(item)
    }
)