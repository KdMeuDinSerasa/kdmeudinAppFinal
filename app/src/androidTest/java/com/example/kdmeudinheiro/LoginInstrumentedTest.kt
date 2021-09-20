package com.example.kdmeudinheiro

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.kdmeudinheiro.view.LoginActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed

import androidx.test.espresso.matcher.RootMatchers.withDecorView


import androidx.test.espresso.assertion.ViewAssertions.matches
import com.example.kdmeudinheiro.utils.ToastMatcher
import org.hamcrest.CoreMatchers.not


@RunWith(AndroidJUnit4::class)
@LargeTest
class LoginInstrumentedTest {
    private lateinit var emailToBeTyped: String
    private lateinit var passToBeTyped: String

    @get:Rule
    var activityRule: ActivityScenarioRule<LoginActivity> =
        ActivityScenarioRule(LoginActivity::class.java)

    lateinit var decorView : View

    @Before
    fun setup() {
        emailToBeTyped = "janeDoe@gmail.com"
        passToBeTyped = "DoeJane21"

        activityRule.scenario.onActivity {
            decorView = it.window.decorView
        }
    }

    @After
    fun teardown() {

    }

    @Test
    fun testEmailAndPassWordValidation(){
        //type text and press button
        onView(withId(R.id.etUserEmail)).perform(typeText(emailToBeTyped))
        onView(withId(R.id.etUserPassword)).perform(typeText(passToBeTyped))
        onView(withId(R.id.btnLogin)).perform(click())
    }

    @Test
    fun testNotValidEmailAppearToast(){
        val expectedMessage = "Preencha Todos os campos"
        //type text and press button
        emailToBeTyped = "janegamil.com"
        onView(withId(R.id.etUserEmail)).perform(typeText(emailToBeTyped))
//        onView(withId(R.id.etUserPassword)).perform(typeText(passToBeTyped))
        onView(withId(R.id.btnLogin)).perform(click())

        onView(withText(expectedMessage)).inRoot(ToastMatcher())
            .check(matches(withText(expectedMessage)))


    }

}