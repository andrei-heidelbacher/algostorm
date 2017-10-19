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
import android.view.SurfaceView
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withChild
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.widget.ImageView

import org.hamcrest.core.IsInstanceOf.instanceOf
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test

class ClientActivityTest {
    @Rule
    @JvmField val rule = ActivityTestRule(MainActivity::class.java)

    /** Espresso waits for all async loaders to finish, so this doesn't work. */
    @Ignore
    @Test fun testSplashLayout() {
        onView(withId(R.id.mainActivitySplash))
                .check(matches(withChild(instanceOf(ImageView::class.java))))
                .check(matches(isDisplayed()))
    }

    @Test fun testMainLayoutAfterSplash() {
        Thread.sleep(2000)
        onView(withId(R.id.mainActivityClient))
                .check(matches(withChild(instanceOf(SurfaceView::class.java))))
                .check(matches(isDisplayed()))
    }
}
