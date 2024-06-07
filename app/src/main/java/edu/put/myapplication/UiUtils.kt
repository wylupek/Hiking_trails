package edu.put.myapplication

import android.content.res.Configuration
import android.content.res.Resources

object UiUtils {
    fun getNumberOfCards(resources: Resources): Int {
        val screenSize = when(resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK) {
            Configuration.SCREENLAYOUT_SIZE_SMALL -> "small"
            Configuration.SCREENLAYOUT_SIZE_NORMAL -> "normal"
            Configuration.SCREENLAYOUT_SIZE_LARGE -> "large"
            Configuration.SCREENLAYOUT_SIZE_XLARGE -> "xlarge"
            else -> "undefined"
        }

        val orientation = when(resources.configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> "portrait"
            Configuration.ORIENTATION_LANDSCAPE -> "landscape"
            else -> "undefined"
        }

        return when {
            screenSize == "xlarge" && orientation == "landscape" -> 6 // For tablets in landscape
            screenSize == "large" && orientation == "landscape" -> 4 // For large devices in landscape
            screenSize == "normal" && orientation == "landscape" -> 3 // For normal devices in landscape
            screenSize == "small" && orientation == "landscape" -> 2 // For small devices in landscape
            screenSize == "xlarge" && orientation == "portrait" -> 4 // For tablets in portrait
            screenSize == "large" && orientation == "portrait" -> 3 // For large devices in portrait
            screenSize == "normal" && orientation == "portrait" -> 2 // For normal devices in portrait
            else -> 1 // Default number of cards
        }
    }
}
