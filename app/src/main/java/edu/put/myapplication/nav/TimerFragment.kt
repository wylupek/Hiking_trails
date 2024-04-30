package edu.put.myapplication.nav

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import edu.put.myapplication.DBHelper
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

    @SuppressLint("Range")
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

        binding.saveButton.setOnClickListener{
            // below we have created
            // a new DBHelper class,
            // and passed context to it
            val db = DBHelper(requireContext(), null)

            // creating variables for values
            // in name and age edit texts
            val name = "AAA"
            val age = binding.elapsedTimeTextView.text.toString()

            // calling method to add
            // name to our database
            db.addName(name, age)

            // Toast to message on the screen
            Toast.makeText(requireContext(), "$name added to database", Toast.LENGTH_LONG).show()
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
            binding.nameAgeView.append(cursor.getString(cursor.getColumnIndex(DBHelper.AGE_COL)) + "\n")

            // moving our cursor to next
            // position and appending values
            while(cursor.moveToNext()){
                binding.nameTextView.append(cursor.getString(cursor.getColumnIndex(DBHelper.NAME_COl)) + "\n")
                binding.nameAgeView.append(cursor.getString(cursor.getColumnIndex(DBHelper.AGE_COL)) + "\n")
            }

            // at last we close our cursor
            cursor.close()
        }

    }
}
