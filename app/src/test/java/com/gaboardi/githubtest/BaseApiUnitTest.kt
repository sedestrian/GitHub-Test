package com.gaboardi.githubtest

import android.os.Build
import com.gaboardi.githubtest.BuildConfig.BASE_URL
import com.gaboardi.githubtest.di.netModule
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.koin.core.context.startKoin
import org.koin.test.KoinTest

abstract class BaseApiUnitTest : KoinTest {
    lateinit var server: MockWebServer

    @Before
    open fun initTest() {
        server = MockWebServer()
        server.start()
        configureDi()
    }

    @After
    open fun shutdown() {
        if (isMockServerEnabled()) {
            server.shutdown()
        }
    }

    open fun configureDi() {
        startKoin { modules(netModule(if (isMockServerEnabled()) server.url("/").toString() else BASE_URL)) }
    }

    open fun mockHttpResponse(fileName: String, responseCode: Int) = server.enqueue(
        MockResponse()
        .setResponseCode(responseCode)
        .setBody(MockResponseFileReader(fileName).content)
    )

    abstract fun isMockServerEnabled(): Boolean
}