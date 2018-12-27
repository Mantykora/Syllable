package eu.mantykora.sylaby.model

data class Level(val syllables: Array<String> = Array(4, {i -> (i +1).toString()}),
                 val result: ArrayList<Int> = ArrayList<Int>(),
                 val resultReversed: ArrayList<Int> = ArrayList<Int>(),
                 val imagePath: String = "") {
}