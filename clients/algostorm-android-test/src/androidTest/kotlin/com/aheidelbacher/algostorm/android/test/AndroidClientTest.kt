/*
 * Copyright (c) 2017  Andrei Heidelbacher <andrei.heidelbacher@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.aheidelbacher.algostorm.android.test

import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout

import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test

import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@Ignore
class AndroidClientTest {
    @Rule
    @JvmField val rule = ActivityTestRule(MainActivity::class.java)
    private lateinit var activity: MainActivity
    private val instrumentation = InstrumentationRegistry.getInstrumentation()

    @Before fun setUp() {
        activity = rule.activity
        instrumentation.callActivityOnCreate(activity, null)
    }

    @Test fun testSplashLayout() {
        instrumentation.runOnMainSync {
            val layout = activity.findViewById(R.layout.activity_main_splash)
                    as? RelativeLayout
            assertNotNull(layout)
            val view = layout!!
            assertEquals(1, view.childCount)
            assertTrue(view.getChildAt(0) is ImageView)
        }
    }

    @Test fun testMainLayoutAfterSplash() {
        Thread.sleep(4000)
        assertNull(activity.findViewById(R.layout.activity_main_splash))
        val layout = activity.findViewById(R.layout.activity_main)
                as? RelativeLayout
        assertNotNull(layout)
        val view = layout!!
        assertEquals(1, view.childCount)
        assertTrue(view.getChildAt(0) is LinearLayout)
    }

    @After fun cleanUp() {
        //instrumentation.callActivityOnDestroy(activity)
    }
}
