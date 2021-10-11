# Archived

Use  [ComposeReorderable](https://github.com/aclassen/ComposeReorderable) , This library is still good and working if you want to look at the source code

# Compose Draggable List

Create a list of items in compose in which items can rearranged by dragging vertically

# Demo

`slow gif`

![ezgif-4-62f54ddef604](https://user-images.githubusercontent.com/42442700/133121261-45971afc-8afb-42c1-86e2-70103534781c.gif)

# Setup

#### Step 1. Put Jitpack repository in your gradle build file

```groovy
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```

#### Step 2. Add the dependency

```groovy
dependencies {
    implementation 'com.github.timeline-notes:compose-draggable-list:1.0.1'
}
```

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

`Modifier.dragger()` is a modifier that sets up a draggable modifier on that composabe You can use
this on the composable that drags the entire item in the column for example you could have drag
indicator icon with this modifier rather than the whole composable
