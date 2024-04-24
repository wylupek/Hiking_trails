package edu.put.myapplication.nav

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import edu.put.myapplication.CardAdapter
import edu.put.myapplication.DetailActivity
import edu.put.myapplication.PARENT
import edu.put.myapplication.TRAIL_ID_EXTRA
import edu.put.myapplication.Trail
import edu.put.myapplication.TrailClickListener
import edu.put.myapplication.databinding.FragmentGridBinding
import edu.put.myapplication.trailList

class HomeFragment : Fragment(), TrailClickListener {
    private lateinit var binding: FragmentGridBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        binding = FragmentGridBinding.inflate(layoutInflater)
        val advancedActivity = this
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = CardAdapter(trailList, advancedActivity)
        }
        return binding.root
    }

    override fun onClick(trail: Trail) {
        val intent = Intent(requireContext(), DetailActivity::class.java)
        intent.putExtra(TRAIL_ID_EXTRA, trail.id)
        intent.putExtra(PARENT, "Home")
        startActivity(intent)
    }
}
