package com.breader.command

import com.breader.command.impl.PingCommand
import com.breader.protocol.type.BulkStringData
import com.breader.protocol.type.ErrorData
import com.breader.protocol.type.SimpleStringData
import kotlin.test.Test
import kotlin.test.assertTrue

class PingCommandTest {

    val command = PingCommand()

    @Test
    fun `should respond with PONG when no argument is provided`() {
        val result = command.execute(listOf(BulkStringData("PING")))

        assertTrue(result is SimpleStringData)
        assertTrue(result.value == "PONG")
    }

    @Test
    fun `should respond with first arg when one arg was provided`() {
        val result = command.execute(listOf(BulkStringData("PING"), BulkStringData("arg")))

        assertTrue(result is BulkStringData)
        assertTrue(result.value == "arg")
    }

    @Test
    fun `should respond with error when more than one arg was provided`() {
        val result = command.execute(listOf(BulkStringData("PING"), BulkStringData("arg1"), BulkStringData("arg2")))

        assertTrue(result is ErrorData)
        assertTrue(result.type == "ARG")
        assertTrue(result.message == "Invalid number of arguments for PING command")
    }
}