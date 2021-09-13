# Compose Draggable List

Create a list of items in compose in which items can rearranged by dragging vertically

# Usage

### Create a list

```kotlin
val foodList = remember {
    draggableStateListOf(
        Food("Apples"),
        Food("Bananas"),
        Food("Blueberries"),
        Food("Oranges"),
        Food("Watermelons")
    )
}
```

### Non Lazy Draggable List
```kotlin
// Use DraggableListIndexed to get index
DraggableList(items = foodList) { item ->
    Row(Modifier.dragger()) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = item.name,
            onValueChange = {
                item.name = it
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(textColor = MaterialTheme.colors.onBackground)
        )
    }
}
```

### Lazy Draggable List
```kotlin
// Lazy List
val scope = rememberCoroutineScope()
var animationsEnabled by remember { mutableStateOf(true) }

LazyColumn {
    draggableItems(
        foodList,
        scope = scope,
        animationsEnabled = animationsEnabled,
        updateAnimationsEnabled = {
            animationsEnabled = it
        }
    ) { item ->
        Row(Modifier.dragger()) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = item.name,
                onValueChange = {
                    item.name = it
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(textColor = MaterialTheme.colors.onBackground)
            )
        }
    }
}
```

`Modifier.dragger()` is a modifier that sets up a draggable modifier on that composabe
You can use this on the composable that drags the entire item in the column
for example you could have drag indicator icon with this modifier rather than the whole composable
