package edu.put.myapplication

import android.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import edu.put.myapplication.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val trailId = intent.getIntExtra(TRAIL_ID_EXTRA, -1)
        val trail = trailFromId(trailId)
        if(trail != null) {
            binding.trailImg.setImageResource(trail.imgId)
            binding.trailName.text = trail.name
            binding.description.text = trail.description
            binding.stages.adapter =  ArrayAdapter(this, R.layout.simple_list_item_1, trail.stages)
        }
    }

    private fun trailFromId(trailId: Int): Trail? {
        for (trail in trailList) {
            if (trail.id == trailId)
                return trail
        }
        return null
    }
}