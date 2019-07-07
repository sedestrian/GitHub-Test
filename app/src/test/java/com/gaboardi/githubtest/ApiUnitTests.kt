package com.gaboardi.githubtest

import com.google.gson.JsonParser
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.get

class ApiUnitTests: KoinTest {
    lateinit var server : MockWebServer

    @Before
    fun initTest(){
        server = MockWebServer()
    }

    @After
    fun shutdown(){
        server.shutdown()
    }


    @Test
    fun `user query sends proper body`(){
        server.apply{
            enqueue(MockResponse().setBody(MockResponseFileReader("user_success.json").content))
        }

        val baseUrl = server.url("")

        //we create the AuthenticationManager using the Base Url provided by the Mock Server
        startKoin(listOf(module {
            single { UserQueryManager(baseUrl.url().toString()) }
        }))

        get<UserQueryManager>().apply {
            queryBlocking()
        }

        val testBody = UserQueryBody(UserQueryManager.username, UserQueryManager.password)
        val requestBody = server.takeRequest().body.readUtf8()

        val json = JsonParser().parse(requestBody).asJsonObject
        assertEquals(json.get("username").toString().replace("\"",""), testBody.username)
    }
}