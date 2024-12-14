package com.breader.command.impl

import com.breader.protocol.type.BulkStringData
import com.breader.protocol.type.ErrorData
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class EchoCommandTest {

    private val command = EchoCommand()

    @Test
    fun `should respond with passed message`() {
        val result = command.execute(listOf(BulkStringData("ECHO"), BulkStringData("Hello, world!")))

        assertTrue(result is BulkStringData)
        assertEquals("Hello, world!", result.value)
    }

    @Test
    fun `should respond with error when number of arguments is invalid`() {
        val result = command.execute(listOf(BulkStringData("ECHO")))

        assertTrue(result is ErrorData)
        assertEquals("ARG", result.type)
        assertEquals("Invalid number of arguments for ECHO command", result.message)
    }
}