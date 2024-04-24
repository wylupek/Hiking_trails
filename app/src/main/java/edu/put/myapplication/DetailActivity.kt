package edu.put.myapplication

import android.os.Bundle
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

    override fun onCreate(savedInstanceState: Bundle?) {
        trailId = intent.getIntExtra(TRAIL_ID_EXTRA, -1)
        val trail = trailList[trailId]
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.supportActionBar?.hide()
        detector = GestureDetector(this, GestureListener())
        generateTrailView(trail)



        binding.swipeLeft.setOnClickListener {
            if(trailId > 0) {
                val newId = findPreviousTrailId(trailId)
                if (newId != null && newId != trailId) {
                    generateTrailView(trailList[newId])
                    trailId -= 1
                }
            }
        }

        binding.swipeRight.setOnClickListener {
            if(trailId < trailList.size - 1) {
                val newId = findNextTrailId(trailId)
                if (newId != null && newId != trailId) {
                    generateTrailView(trailList[newId])
                    trailId += 1
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
        if (category == "Home") {
            return if (currentId < trailList.size - 1) {
                currentId + 1
            } else {
                null
            }
        }

        for (i in currentId + 1 until trailList.size) {
            val trail = trailList[i]
            if (trail.difficulty == category) {
                return trail.id
            }
        }
        return null
    }

    private fun findPreviousTrailId(currentId: Int): Int? {
        val category = intent.getStringExtra(PARENT)
        if (category == "Home") {
            return if (currentId > 0) {
                currentId - 1
            } else {
                null
            }
        }

        for (i in currentId - 1 downTo 0) {
            val trail = trailList[i]
            if (trail.difficulty == category) {
                return trail.id
            }
        }
        return null
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
                trailId += 1
            }
        }
    }

    private fun onSwipeRight() {
        if(trailId > 0) {
            val newId = findPreviousTrailId(trailId)
            if (newId != null && newId != trailId) {
                generateTrailView(trailList[newId])
                trailId -= 1
            }
        }
    }

}