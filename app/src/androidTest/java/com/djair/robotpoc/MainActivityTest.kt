package com.djair.robotpoc

import android.content.Intent

import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.core.AllOf
import org.junit.Test

class MainActivityTest {

    fun launchActivity() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
        androidx.test.core.app.launchActivity<MainActivity>(intent)
    }

    @Test
    fun verify_some(){
//        Berfore

        launchActivity()
//        OnAction

//        Verify
        onView(withId(R.id.nav_host_fragment_activity_main))
            .check(matches(isDisplayed()))

    }

    @Test
    fun verify_some_2(){
//        Berfore

        launchActivity()
//        OnAction
        onView(AllOf.allOf(withText("Dashboard"), isDisplayed())).perform(ViewActions.click())

//        Verify
        onView(withText("Dashboard"))
            .check(matches(isDisplayed()))

    }


}