package com.djair.robotpoc

import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.djair.robotpoc.rules.ActivityRule
import com.djair.robotpoc.ui.home.HomeViewModel
import org.hamcrest.core.AllOf
import org.junit.Rule
import org.junit.Test
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

class MainActivityTest {

    @get:Rule
    val activityRule = ActivityRule(MainActivity::class.java)

    private val viewModel = HomeViewModel()

    fun launchActivity() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
        androidx.test.core.app.launchActivity<MainActivity>(intent)
    }

    @Test
    fun verify_some() {
//        Berfore

        launchActivity()
//        OnAction

//        Verify
        onView(withId(R.id.nav_host_fragment_activity_main))
            .check(matches(isDisplayed()))

    }

    @Test
    fun verify_some_2() {
//        Berfore

        launchActivity()
//        OnAction
        onView(AllOf.allOf(withText("Dashboard"), isDisplayed())).perform(ViewActions.click())

//        Verify
        onView(withText("Dashboard"))
            .check(matches(isDisplayed()))

    }

    @Test
    fun isViewVisible_Should_get_success_When_Activity_is_visible() {
        val presentationModule = module { viewModel { viewModel } }
        activityRule.setModules {
            presentationModule
        }.putArguments {
            putString("KEY", "Hello World")
        }.launchActivity()

        onView(withId(R.id.nav_host_fragment_activity_main))
            .check(matches(isDisplayed()))
    }
}
