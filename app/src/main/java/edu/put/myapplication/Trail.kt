package edu.put.myapplication

var trailList = mutableListOf<Trail>()

val TRAIL_ID_EXTRA = "trailExtra"
val PARENT = "parent"
val SEARCH_QUERY = ""

class Trail (
    var name: String,
    var description: String,
    var img: String,
    var imgId: Int,
    var difficulty: String,
    var stages: List<String>,
    var distances: List<Int>,
    var distance: Int = distances.sum(),
    val id: Int? = trailList.size
)