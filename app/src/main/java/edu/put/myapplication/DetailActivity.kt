package edu.put.myapplication

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import edu.put.myapplication.databinding.ActivityDetailBinding
import kotlin.math.abs

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var detector: GestureDetector
    private var trailId = 0
    private var button_state = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        trailId = intent.getIntExtra(TRAIL_ID_EXTRA, -1)
        val trail = trailList[trailId]
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.supportActionBar?.hide()
        detector = GestureDetector(this, GestureListener())
        generateTrailView(trail)

        binding.detailItem.paceButton.setOnClickListener() {
            when (button_state) {
                0 -> {
                    binding.detailItem.paceButton.text = "Average"
                    binding.detailItem.paceButton.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.black))
                    button_state = 1
                }
                1 -> {
                    binding.detailItem.paceButton.text = "Fast"
                    binding.detailItem.paceButton.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.black))
                    button_state = 2
                }
                2 -> {
                    binding.detailItem.paceButton.text = "Slow"
                    binding.detailItem.paceButton.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.black))
                    button_state = 0
                }
            }
        }


        binding.swipeLeft.setOnClickListener {
            if(trailId > 0) {
                val newId = findPreviousTrailId(trailId)
                if (newId != null && newId != trailId) {
                    generateTrailView(trailList[newId])
                    trailId = newId
                }
            }
        }

        binding.swipeRight.setOnClickListener {
            if(trailId < trailList.size - 1) {
                val newId = findNextTrailId(trailId)
                if (newId != null && newId != trailId) {
                    generateTrailView(trailList[newId])
                    trailId = newId
                }
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return if (event?.let { detector.onTouchEvent(it) } == true) {
            true
        } else {
            return super.onTouchEvent(event)
        }

    }
    private fun findNextTrailId(currentId: Int): Int? {
        val category = intent.getStringExtra(PARENT)
        val searchQuery = intent.getStringExtra(SEARCH_QUERY)

        return trailList.firstOrNull { trail ->
            if (category == "Home") {
                searchQuery?.let { trail.name.contains(it, ignoreCase = true) } ?: true &&
                        trail.id != null && trail.id > currentId
            } else {
                searchQuery?.let { trail.name.contains(it, ignoreCase = true) } ?: true &&
                        trail.difficulty == category &&
                        trail.id != null && trail.id > currentId
            }
        }?.id
    }

    private fun findPreviousTrailId(currentId: Int): Int? {
        val category = intent.getStringExtra(PARENT)
        val searchQuery = intent.getStringExtra(SEARCH_QUERY)
        return trailList.lastOrNull { trail ->
            if (category == "Home") {
                searchQuery?.let { trail.name.contains(it, ignoreCase = true) } ?: true &&
                        trail.id != null && trail.id < currentId
            } else {
                trail.difficulty == category &&
                        searchQuery?.let { trail.name.contains(it, ignoreCase = true) } ?: true &&
                        trail.id != null && trail.id < currentId
            }
        }?.id
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        super.dispatchTouchEvent(ev)
        if (ev == null) {
            return false
        }
        return detector.onTouchEvent(ev)
    }

    private fun generateTrailView(trail: Trail) {
        binding.detailItem.toolbar.title = trail.difficulty
        binding.detailItem.trailImg.setImageResource(trail.imgId)
        binding.detailItem.trailName.text = trail.name
        binding.detailItem.description.text = trail.description
        val stagesAdapter = StagesAdapter(trail.stages)
        binding.detailItem.stages.adapter = stagesAdapter
        binding.detailItem.stages.layoutManager = LinearLayoutManager(this)
    }

    inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        private val swipeThreshold = 100
        private val swipeVelocityThreshold = 100
        override fun onFling(
            downEvent: MotionEvent?,
            moveEvent: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            val diffX = moveEvent.x.minus(downEvent!!.x)
            val diffY = moveEvent.y.minus(downEvent.y)

            if (abs(diffX) > abs(diffY)) {
                if (abs(diffX) > swipeThreshold && abs(velocityX) > swipeVelocityThreshold) {
                    if (diffX > 0) {
                        this@DetailActivity.onSwipeRight()
                    }
                    else {
                        this@DetailActivity.onSwipeLeft()
                    }
                }
            }
            return super.onFling(downEvent, moveEvent, velocityX, velocityY)
        }
    }

    private fun onSwipeLeft() {
        if(trailId < trailList.size - 1) {
            val newId = findNextTrailId(trailId)
            if (newId != null && newId != trailId) {
                generateTrailView(trailList[newId])
                trailId = newId
            }
        }
    }

    private fun onSwipeRight() {
        if(trailId > 0) {
            val newId = findPreviousTrailId(trailId)
            if (newId != null && newId != trailId) {
                generateTrailView(trailList[newId])
                trailId = newId
            }
        }
    }

}