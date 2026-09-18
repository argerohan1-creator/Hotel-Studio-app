package com.example

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToString
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.RobolectricTestRunner
import com.example.ui.screens.SignageBuilderScreen
import com.example.viewmodel.HotelStudioViewModel
import androidx.test.core.app.ApplicationProvider
import android.app.Application

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
class SignageBuilderScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun screenRendersSuccessfully() {
        val application = ApplicationProvider.getApplicationContext<Application>()
        val viewModel = HotelStudioViewModel(application)
        composeTestRule.setContent {
            SignageBuilderScreen(viewModel = viewModel)
        }
        println("UI Tree:")
        println(composeTestRule.onRoot().printToString())
    }
}
