package com.breader.command

import com.breader.engine.Storage
import com.breader.protocol.type.ArrayData
import com.breader.protocol.type.BulkStringData
import com.breader.protocol.type.SimpleStringData
import io.mockk.mockk
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class CommandDispatcherTest {

    private val storage = mockk<Storage>()
    private val dispatcher = CommandDispatcher(storage)

    @Test
    fun `should dispatch command`() {
        // given
        val incomingPingCommand = ArrayData(listOf(BulkStringData("PING")))

        // when
        val result = dispatcher.dispatch(incomingPingCommand)

        // then
        assertTrue(result is SimpleStringData)
        assertEquals("PONG", result.value)
    }

    @Test
    fun `should dispatch command written with another case`() {
        // given
        val incomingPingCommand = ArrayData(listOf(BulkStringData("ping")))

        // when
        val result = dispatcher.dispatch(incomingPingCommand)

        // then
        assertTrue(result is SimpleStringData)
        assertEquals("PONG", result.value)
    }

    @Test
    fun `should throw exception when unknown command`() {
        // given
        val incomingUnknownCommand = ArrayData(listOf(BulkStringData("UNKNOWN")))

        // when + then
        assertFailsWith<IllegalArgumentException> {
            dispatcher.dispatch(incomingUnknownCommand)
        }
    }

    @Test
    fun `should throw exception when command is not an array`() {
        // given
        val incomingNotArrayCommand = BulkStringData("PING")

        // when + then
        assertFailsWith<IllegalArgumentException> {
            dispatcher.dispatch(incomingNotArrayCommand)
        }
    }
}