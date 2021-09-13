package com.wakaztahir.draggablelist

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList


fun <T> mutableStateListOfDraggableItems(vararg elements: T): SnapshotStateList<DraggableListItem<T>> =
    mutableStateListOf(
        *elements.map { DraggableListItem(it) }.toTypedArray()
    )

fun <T> mutableStateListOfDraggableItems(): SnapshotStateList<DraggableListItem<T>> =
    mutableStateListOf()
