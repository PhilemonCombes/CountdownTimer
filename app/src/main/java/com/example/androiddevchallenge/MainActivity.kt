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
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.androiddevchallenge.ui.theme.MyTheme
import com.example.androiddevchallenge.ui.theme.darkBlue
import com.example.androiddevchallenge.ui.theme.red
import com.example.androiddevchallenge.ui.theme.transparentWhite

class MainActivity : AppCompatActivity() {

    @ExperimentalAnimationApi
    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyTheme {
                MyApp()
            }
        }
    }
}

// Start building your app here!
@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@Composable
fun MyApp() {
    val isCountDownActive = remember { mutableStateOf(false) }
    val timerValue = remember { mutableStateOf(5000L) }

    Surface(color = darkBlue) {

        val timer = object : CountDownTimer(timerValue.value, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                if (isCountDownActive.value)
                    timerValue.value = millisUntilFinished
            }

            override fun onFinish() {
                isCountDownActive.value = false
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {

            BoxWithConstraints {
                val boxHeight = with(LocalDensity.current) { constraints.maxHeight.toDp() }
                val size by animateDpAsState(
                    targetValue = if (isCountDownActive.value) 0.dp else boxHeight,
                    animationSpec = tween(
                        durationMillis = if (isCountDownActive.value) timerValue.value.toInt() else 300,
                        easing = LinearEasing
                    )
                )

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .requiredHeight(size)
                            .background(red),
                    )
                }
            }

            ConstraintLayout(modifier = Modifier.fillMaxSize()) {

                val (timerTitle, timerRow, button) = createRefs()

                Text(
                    text = timerValue.value.toReadableTime(),
                    style = TextStyle(color = Color.White, fontSize = 120.sp),
                    modifier = Modifier.constrainAs(timerTitle) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }
                )

                val possibleTimes =
                    listOf(5000L, 10000L, 20000L, 30000L, 40000L, 50000L, 60000L, 90000L, 120000L)

                AnimatedVisibility(
                    visible = !isCountDownActive.value,
                    modifier = Modifier
                        .constrainAs(timerRow) {
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(button.top, margin = 36.dp)
                        },
                    enter = fadeIn(), exit = fadeOut()
                ) {
                    Row(
                        Modifier
                            .padding(horizontal = 16.dp)
                            .horizontalScroll(rememberScrollState())
                    ) {
                        val selectedIndex = remember { mutableStateOf(-1) }
                        possibleTimes.forEachIndexed { index, possibleTime ->
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                                Text(
                                    text = possibleTime.toReadableTime(),
                                    style = TextStyle(
                                        color = if (selectedIndex.value == index) Color.White else transparentWhite,
                                        fontSize = 32.sp
                                    ),
                                    modifier = Modifier
                                        .clickable {
                                            timerValue.value = possibleTime
                                            selectedIndex.value = index
                                        }
                                        .padding(6.dp)
                                )

                                Box(
                                    modifier = Modifier
                                        .background(
                                            shape = RoundedCornerShape(4.dp),
                                            color = if (selectedIndex.value == index) darkBlue else Color.Transparent
                                        )
                                        .requiredHeight(8.dp)
                                        .requiredWidth(50.dp)
                                )
                            }
                        }
                    }
                }

                Button(
                    onClick = {
                        if (isCountDownActive.value) timer.cancel() else timer.start()
                        isCountDownActive.value = !isCountDownActive.value
                    },
                    shape = CircleShape,
                    modifier = Modifier.constrainAs(button) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom, margin = 24.dp)
                    },
                    colors = object : ButtonColors {
                        @Composable
                        override fun backgroundColor(enabled: Boolean): State<Color> =
                            rememberUpdatedState(if (isCountDownActive.value) Color.White else darkBlue)

                        @Composable
                        override fun contentColor(enabled: Boolean): State<Color> =
                            rememberUpdatedState(Color.Transparent)
                    }
                ) {
                    Box(
                        modifier = Modifier
                            .requiredSize(60.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (isCountDownActive.value) "Stop" else "Start",
                            style = TextStyle(color = if (isCountDownActive.value) red else Color.White),
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp
                        )
                    }
                }
            }
        }
    }
}
