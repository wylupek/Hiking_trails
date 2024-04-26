package edu.put.myapplication

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TimerViewModel : ViewModel() {
    private val _elapsedTimeMillis = MutableLiveData<Long>()
    val elapsedTimeMillis: LiveData<Long>
        get() = _elapsedTimeMillis

    var isTimerRunning = false
    private var startTimeMillis = 0L
    private val handler = Handler(Looper.getMainLooper())

    init {
        _elapsedTimeMillis.value = 0
    }

    fun startTimer() {
        if (!isTimerRunning) {
            isTimerRunning = true
            startTimeMillis = System.currentTimeMillis() - (_elapsedTimeMillis.value ?: 0)
            updateTimer()
        }
    }

    fun stopTimer() {
        if (isTimerRunning) {
            isTimerRunning = false
            handler.removeCallbacksAndMessages(null)
        }
    }

    fun resetTimer() {
        isTimerRunning = false
        _elapsedTimeMillis.value = 0
        handler.removeCallbacksAndMessages(null)
    }

    private fun updateTimer() {
        if (isTimerRunning) {
            _elapsedTimeMillis.value = System.currentTimeMillis() - startTimeMillis
            handler.postDelayed({ updateTimer() }, 1000)
        }
    }
}
