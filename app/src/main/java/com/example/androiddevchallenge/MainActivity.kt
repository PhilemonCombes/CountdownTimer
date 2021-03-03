/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androiddevchallenge.ui.theme.MyTheme
import kotlin.math.round

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            var timerValue by remember { mutableStateOf(0) }

            val timer = object : CountDownTimer(5000, 1000) {

                override fun onTick(millisUntilFinished: Long) {
                    timerValue = round(millisUntilFinished / 1000f).toInt()
                }

                override fun onFinish() {
                    Toast.makeText(baseContext, "ALEEEEERTE", Toast.LENGTH_SHORT).show()
                }
            }

            MyTheme {
                MyApp(timerValue, timer)
            }
        }
    }
}

// Start building your app here!
@Composable
fun MyApp(timerValue: Int, timer: CountDownTimer) {

    var state by remember { mutableStateOf(false) }
    val transition = updateTransition(targetState = state)

    Surface(color = MaterialTheme.colors.background) {

        Box(modifier = Modifier.fillMaxSize()) {

            BoxWithConstraints {
                val boxHeight = with(LocalDensity.current) { constraints.maxHeight.toDp() }
                val size by animateDpAsState(
                    targetValue = if (state) 0.dp else boxHeight,
                    animationSpec = tween(durationMillis = 5000, easing = LinearEasing)
                )

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .requiredHeight(size)
                            .background(Color.Blue),
                    ) {
                    }
                }
            }

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Text(
                    text = timerValue.toString(),
                    style = TextStyle(color = Color.White, fontSize = 100.sp),
                    modifier = Modifier
                )

                Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                    Text(text = "00:10", style = TextStyle(color = Color.White, fontSize = 32.sp))
                    Text(text = "00:20", style = TextStyle(color = Color.White, fontSize = 32.sp))
                    Text(text = "00:30", style = TextStyle(color = Color.White, fontSize = 32.sp))
                    Text(text = "00:40", style = TextStyle(color = Color.White, fontSize = 32.sp))
                    Text(text = "00:50", style = TextStyle(color = Color.White, fontSize = 32.sp))
                    Text(text = "01:00", style = TextStyle(color = Color.White, fontSize = 32.sp))
                }

                Button(
                    onClick = {
                        timer.start()
                        state = !state
                    }
                ) {
                    Text(text = "Start")
                }
            }
        }
    }
}
