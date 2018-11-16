package eu.mantykora.sylaby.model

data class Placeholder(val placeholderIndex: Int,
                       val placeholderDimensionX: Int,
                       val placeholderDimensionY: Int,
                       var isFree: Boolean = true,
                       val syllableIndex: Int = -1) {
}