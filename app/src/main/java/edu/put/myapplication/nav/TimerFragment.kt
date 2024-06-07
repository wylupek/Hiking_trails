package edu.put.myapplication.nav

import android.annotation.SuppressLint
import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import edu.put.myapplication.DBHelper
import edu.put.myapplication.R
import edu.put.myapplication.TimerViewModel
import edu.put.myapplication.databinding.FragmentTimerBinding
import edu.put.myapplication.trailList
import android.widget.ArrayAdapter
import edu.put.myapplication.Trail

class TimerFragment : Fragment() {
    private lateinit var timerViewModel: TimerViewModel
    private lateinit var binding: FragmentTimerBinding
    private var startStopState = 1
    private lateinit var avd: AnimatedVectorDrawableCompat
    private lateinit var avd2: AnimatedVectorDrawable
    private lateinit var selectedTrail: Trail

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTimerBinding.inflate(inflater, container, false)

        val trailNames = trailList.map { it.name }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, trailNames)
        binding.trailAutoCompleteTextView.setAdapter(adapter)
        binding.trailAutoCompleteTextView.setOnItemClickListener { parent, _, position, _ ->
            val selectedTrailName = parent.getItemAtPosition(position) as String
            selectedTrail = trailList.first { it.name == selectedTrailName }
        }

        return binding.root
    }

    @SuppressLint("Range")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        timerViewModel = ViewModelProvider(this)[TimerViewModel::class.java]

        timerViewModel.elapsedTimeMillis.observe(viewLifecycleOwner) { elapsedTimeMillis ->
            val hours = (elapsedTimeMillis / 3600000) % 24
            val minutes = (elapsedTimeMillis / 60000) % 60
            val seconds = (elapsedTimeMillis / 1000) % 60
            binding.elapsedTimeTextView.text = String.format("%02d:%02d:%02d", hours, minutes, seconds)
        }


        binding.startStopTimeButton.setOnClickListener {
            toggleStartStop()
        }


        binding.resetTimeButton.setOnClickListener {
            timerViewModel.resetTimer()
            if (startStopState == 0) {
                toggleStartStop()
            }
        }

        binding.saveTimeButton.setOnClickListener{
            val db = DBHelper(requireContext(), null)

            val time = binding.elapsedTimeTextView.text.toString()
            if (::selectedTrail.isInitialized) {
                db.addTime(selectedTrail.name, time)
                Toast.makeText(requireContext(), "${selectedTrail.name} added to database", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(requireContext(), "Select trail before you save the time!", Toast.LENGTH_LONG).show()
            }
        }
        binding.printButton.setOnClickListener{

            // creating a DBHelper class
            // and passing context to it
            val db = DBHelper(requireContext(), null)

            // below is the variable for cursor
            // we have called method to get
            // all names from our database
            // and add to name text view
            val cursor = db.getName()

            // moving the cursor to first position and
            // appending value in the text view
            cursor!!.moveToFirst()
            binding.nameTextView.append(cursor.getString(cursor.getColumnIndex(DBHelper.NAME_COl)) + "\n")
            binding.nameAgeView.append(cursor.getString(cursor.getColumnIndex(DBHelper.TIME_COL)) + "\n")

            // moving our cursor to next
            // position and appending values
            while(cursor.moveToNext()){
                binding.nameTextView.append(cursor.getString(cursor.getColumnIndex(DBHelper.NAME_COl)) + "\n")
                binding.nameAgeView.append(cursor.getString(cursor.getColumnIndex(DBHelper.TIME_COL)) + "\n")
            }

            // at last we close our cursor
            cursor.close()
        }
    }

    private fun toggleStartStop() {
        startStopState = if (startStopState == 0) {
            setButtonDrawable(R.drawable.ic_stop)
            timerViewModel.stopTimer()
            1
        } else {
            setButtonDrawable(R.drawable.ic_start)
            timerViewModel.startTimer()
            0
        }
    }

    private fun setButtonDrawable(drawableRes: Int) {
        binding.startStopTimeButton.setImageDrawable(
            ContextCompat.getDrawable(requireContext(), drawableRes)
        )

        when (val drawable = binding.startStopTimeButton.drawable) {
            is AnimatedVectorDrawableCompat -> {
                avd = drawable
                avd.start()
            }
            is AnimatedVectorDrawable -> {
                avd2 = drawable
                avd2.start()
            }
        }
    }
}
