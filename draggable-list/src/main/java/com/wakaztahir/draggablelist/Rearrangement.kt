package com.wakaztahir.draggablelist

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Rearranges other items as one item is being dragged
 */
internal fun <T> rearrangeItems(
    items: List<DraggableListItem<T>>,
    index: Int,
    item: DraggableListItem<T>,
    yOffset: Dp
) {
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
