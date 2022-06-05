import androidx.compose.desktop.DesktopMaterialTheme
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.wakaztahir.common.draggableItemsIndexed
import com.wakaztahir.common.draggableStateListOf

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        DesktopMaterialTheme {
            var list = draggableStateListOf("sdf", "sdf", "sdf", "sdfsdfasdf", "dslkfjsalkdfj", "slkdjflkds")

            var animationsEnabled by remember { mutableStateOf(true) }

            val scope = rememberCoroutineScope()

            LazyColumn {
                draggableItemsIndexed(
                    list,
                    animationsEnabled = animationsEnabled,
                    updateAnimationsEnabled = {
                        animationsEnabled = it
                    },
                    scope = scope
                ) { index, item ->
                    Text(
                        modifier = Modifier.dragger(),
                        text = "$index $item"
                    )
                }
            }
        }
    }
}