package com.gaboardi.githubtest

import org.junit.Assert.assertEquals
import org.junit.Test

class TestMockResponseFileReader {
    @Test
    fun `read simple file`(){
        val reader = MockResponseFileReader("test.json")
        assertEquals(reader.content, "success")
    }
}