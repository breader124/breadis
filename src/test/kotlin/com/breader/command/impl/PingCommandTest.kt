package com.breader.command.impl

import com.breader.protocol.type.BulkStringData
import com.breader.protocol.type.ErrorData
import com.breader.protocol.type.SimpleStringData
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PingCommandTest {

    val command = PingCommand()

    @Test
    fun `should respond with PONG when no argument is provided`() {
        val result = command.execute(listOf(BulkStringData("PING")))

        assertTrue(result is SimpleStringData)
        assertEquals("PONG", result.value)
    }

    @Test
    fun `should respond with first arg when one arg was provided`() {
        val result = command.execute(listOf(BulkStringData("PING"), BulkStringData("arg")))

        assertTrue(result is BulkStringData)
        assertEquals("arg", result.value)
    }

    @Test
    fun `should respond with error when more than one arg was provided`() {
        val result = command.execute(listOf(BulkStringData("PING"), BulkStringData("arg1"), BulkStringData("arg2")))

        assertTrue(result is ErrorData)
        assertEquals("ARG", result.type)
        assertEquals("Invalid number of arguments for PING command", result.message)
    }
}