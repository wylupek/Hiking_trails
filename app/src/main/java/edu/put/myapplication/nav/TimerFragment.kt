package edu.put.myapplication.nav

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import edu.put.myapplication.TimerViewModel
import edu.put.myapplication.databinding.FragmentTimerBinding

class TimerFragment : Fragment() {
    private lateinit var timerViewModel: TimerViewModel
    private lateinit var binding: FragmentTimerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTimerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        timerViewModel = ViewModelProvider(this).get(TimerViewModel::class.java)

        timerViewModel.elapsedTimeMillis.observe(viewLifecycleOwner) { elapsedTimeMillis ->
            val hours = (elapsedTimeMillis / 3600000) % 24
            val minutes = (elapsedTimeMillis / 60000) % 60
            val seconds = (elapsedTimeMillis / 1000) % 60
            binding.elapsedTimeTextView.text = String.format("%02d:%02d:%02d", hours, minutes, seconds)
        }

        binding.startStopButton.setOnClickListener {
            if (timerViewModel.isTimerRunning) {
                timerViewModel.stopTimer()
                binding.startStopButton.text = "Start"
            } else {
                timerViewModel.startTimer()
                binding.startStopButton.text = "Stop"
            }
        }

        binding.resetButton.setOnClickListener {
            timerViewModel.resetTimer()
            binding.startStopButton.text = "Start"
        }
    }
}
