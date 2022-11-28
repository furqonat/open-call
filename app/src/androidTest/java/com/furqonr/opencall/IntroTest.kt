package com.furqonr.opencall

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.furqonr.opencall.ui.screens.intro.Intro
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class IntroTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()


    @Before
    fun setUp() {
        composeTestRule.setContent {
            Intro {
                // do nothing
            }
        }
    }

    @Test
    fun testWelcome() {
        composeTestRule.onNodeWithText("Welcome to OpenCall").assertExists()
        composeTestRule.onNodeWithText("Get Started").assertExists()
    }

    @Test
    fun testClickGetStarted() {
        composeTestRule.onNode(hasText("Get Started")).performClick()
        composeTestRule.onNodeWithText("Welcome to OpenCall").assertDoesNotExist()
    }

}