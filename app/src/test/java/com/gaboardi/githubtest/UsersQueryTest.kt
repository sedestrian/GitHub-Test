package com.gaboardi.githubtest

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O])
class UsersQueryTest: BaseApiUnitTest() {
    override fun isMockServerEnabled(): Boolean = true

    @get:Rule
    val rule = InstantTaskExecutorRule()

    fun `user query has correct body`(){
        mockHttpResponse()
    }

    fun `user query response sets data correctly`(){
    }

    fun `user query error is set correctly`(){
    }
}