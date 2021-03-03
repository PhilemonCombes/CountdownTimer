package com.example.androiddevchallenge

import android.os.CountDownTimer
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    var timerValue = 0L

    val timer = object : CountDownTimer(20000, 1000) {

        override fun onTick(millisUntilFinished: Long) {
            timerValue = millisUntilFinished
        }

        override fun onFinish() {
        }
    }
}

