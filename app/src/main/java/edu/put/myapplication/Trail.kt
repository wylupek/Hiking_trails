package edu.put.myapplication

var trailList = mutableListOf<Trail>()

val TRAIL_ID_EXTRA = "trailExtra"

class Trail (
    var name: String,
    var description: String,
    var img: String,
    var imgId: Int,
    var difficulty: String,
    var stages: List<String>,
    val id: Int? = trailList.size
)