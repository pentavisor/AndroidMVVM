package com.example.apptest2019.fragments.fragment_second

import android.content.Intent
import android.os.Bundle
import androidx.navigation.NavDeepLinkBuilder
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.example.apptest2019.R
import com.example.apptest2019.activity.MainActivity
import org.junit.After
import org.junit.Before

import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FragmentSecondTest {

    @Before
    fun setUp() {
    }

    @Rule
    @JvmField
    var activityRule = ActivityTestRule(MainActivity::class.java)



    @Test
    fun setTest(){

    }

    @After
    fun tearDown() {
    }
}