package edu.put.myapplication

import android.os.Bundle
import android.view.GestureDetector
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import edu.put.myapplication.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.supportActionBar?.hide()


        var trailId = intent.getIntExtra(TRAIL_ID_EXTRA, -1)

        val trail = trailList[trailId]
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

    }

}