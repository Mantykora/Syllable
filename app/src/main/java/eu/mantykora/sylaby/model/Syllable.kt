package eu.mantykora.sylaby.model

import android.view.View

data class Syllable(val content: String = "",
                    val buttonIndex: Int = 0,
                    val placeholderIndex: Int = -1,
                    val buttonDimensionX: Int = 0,
                    val buttonDimensionY: Int = 0,
                    val isMoved: Boolean = false,
                    val id: Int
                   ) {

}