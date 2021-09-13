package com.wakaztahir.draggablelist

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Clear offsets of all items & determines the new index for [item]
 * @return Returns new index for [item]
 */
internal fun <T> fixedResetItems(
    items: List<DraggableListItem<T>>,
    index: Int,
    yOffset: Dp
) : Int {
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

    return newIndex
}