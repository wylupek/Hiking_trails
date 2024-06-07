package edu.put.myapplication

import RecyclerViewItemDecoration
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import edu.put.myapplication.databinding.ActivityDetailBinding
import kotlin.math.abs

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var detector: GestureDetector
    private var trailId = 0
    private var buttonState = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        trailId = intent.getIntExtra(TRAIL_ID_EXTRA, -1)
        val trail = trailList[trailId]
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.supportActionBar?.hide()
        detector = GestureDetector(this, GestureListener())
        drawTrailView(trail)

        binding.detailItem.paceButton.setOnClickListener() {
            when (buttonState) {
                0 -> {
                    binding.detailItem.paceButton.setText(R.string.pace_slow);
                    binding.detailItem.paceButton.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.green_500)
                    PACE = "Slow"
                    drawStagesView(trailList[trailId], false)
                    buttonState = 1
                }
                1 -> {
                    binding.detailItem.paceButton.setText(R.string.pace_average);
                    binding.detailItem.paceButton.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.green_500)
                    PACE = "Average"
                    drawStagesView(trailList[trailId], false)
                    buttonState = 2
                }
                2 -> {
                    binding.detailItem.paceButton.setText(R.string.pace_fast);
                    binding.detailItem.paceButton.backgroundTintList = ContextCompat.getColorStateList(applicationContext, R.color.green_500)
                    PACE = "Fast"
                    drawStagesView(trailList[trailId], false)
                    buttonState = 0
                }
            }
        }

        binding.swipeLeft.setOnClickListener {
            if(trailId > 0) {
                val newId = findPreviousTrailId(trailId)
                if (newId != null && newId != trailId) {
                    drawTrailView(trailList[newId])
                    trailId = newId
                }
            }
        }

        binding.swipeRight.setOnClickListener {
            if(trailId < trailList.size - 1) {
                val newId = findNextTrailId(trailId)
                if (newId != null && newId != trailId) {
                    drawTrailView(trailList[newId])
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

    private fun drawStagesView(trail: Trail, initial: Boolean) {
        val stagesAdapter = StagesAdapter(trail.stages, trail.distances)
        binding.detailItem.stages.adapter = stagesAdapter
        binding.detailItem.stages.addItemDecoration(RecyclerViewItemDecoration(this, R.drawable.divider))
        binding.detailItem.stages.layoutManager = LinearLayoutManager(this)

        if (!initial) { return }

        val stagesHeader = binding.detailItem.stagesHeader
        val dropdownIcon = binding.detailItem.dropdownIconStages
        binding.detailItem.stages.visibility = View.GONE

        stagesHeader.setOnClickListener {
            if (binding.detailItem.stages.visibility == View.VISIBLE) {
                binding.detailItem.stages.visibility = View.GONE
                dropdownIcon.setImageResource(R.drawable.ic_arrow_down)
            } else {
                binding.detailItem.stages.visibility = View.VISIBLE
                dropdownIcon.setImageResource(R.drawable.ic_arrow_up)
            }
        }
    }

    private fun drawTimesView(trail: Trail) {
        val db = DBHelper(this, null)
        val cursor = db.getTimes(trail.name)
        cursor?.moveToFirst()
        val stagesAdapter = TimesAdapter(this, cursor)
        binding.detailItem.times.adapter = stagesAdapter
        binding.detailItem.times.addItemDecoration(RecyclerViewItemDecoration(this, R.drawable.divider))
        binding.detailItem.times.layoutManager = LinearLayoutManager(this)

        if (cursor?.moveToFirst() == null) {
            binding.detailItem.timesHeader.visibility = View.GONE
            return
        }

        binding.detailItem.timesHeader.visibility = View.VISIBLE

        val stagesHeader = binding.detailItem.timesHeader
        val dropdownIcon = binding.detailItem.dropdownIconTimes
        binding.detailItem.times.visibility = View.GONE

        stagesHeader.setOnClickListener {
            if (binding.detailItem.times.visibility == View.VISIBLE) {
                binding.detailItem.times.visibility = View.GONE
                dropdownIcon.setImageResource(R.drawable.ic_arrow_down)
            } else {
                binding.detailItem.times.visibility = View.VISIBLE
                dropdownIcon.setImageResource(R.drawable.ic_arrow_up)
            }
        }
    }

    private fun drawTrailView(trail: Trail) {
        binding.detailItem.toolbar.title = trail.difficulty
        binding.detailItem.trailImg.setImageResource(trail.imgId)
        binding.detailItem.trailName.text = trail.name
        binding.detailItem.description.text = trail.description
        binding.detailItem.paceButton.text = PACE;
        drawStagesView(trail, true)
        drawTimesView(trail)
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
                drawTrailView(trailList[newId])
                trailId = newId
            }
        }
    }

    private fun onSwipeRight() {
        if(trailId > 0) {
            val newId = findPreviousTrailId(trailId)
            if (newId != null && newId != trailId) {
                drawTrailView(trailList[newId])
                trailId = newId
            }
        }
    }

}