package com.breader.command.impl

import com.breader.engine.Storage
import com.breader.protocol.type.BulkStringData
import com.breader.protocol.type.ErrorData
import com.breader.protocol.type.SimpleStringData
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class SetCommandTest {

    private val storage = mockk<Storage>()
    private val fixedClock = Clock.fixed(Instant.now(), ZoneId.systemDefault())

    private val command = SetCommand(storage, fixedClock)

    @Test
    fun `should set value in storage`() {
        // given
        every { storage.set(any(), any()) } returns null

        // when
        val result = command.execute(
            listOf(
                BulkStringData("SET"),
                BulkStringData("key"),
                BulkStringData("value")
            )
        )

        // then
        assertIs<SimpleStringData>(result)
        assertEquals("OK", result.value)
    }

    @Test
    fun `should set value with expiration time in seconds`() {
        // given
        every { storage.setExpiring(any(), any(), any()) } returns null

        // when
        command.execute(
            listOf(
                BulkStringData("SET"),
                BulkStringData("key"),
                BulkStringData("value"),
                BulkStringData("EX"),
                BulkStringData("10")
            )
        )

        // then
        verify {
            storage.setExpiring("key", "value", Instant.now(fixedClock).plusSeconds(10))
        }

        confirmVerified(storage)
    }

    @Test
    fun `should set value with expiration time in millis`() {
        // given
        every { storage.setExpiring(any(), any(), any()) } returns null

        // when
        command.execute(
            listOf(
                BulkStringData("SET"),
                BulkStringData("key"),
                BulkStringData("value"),
                BulkStringData("PX"),
                BulkStringData("10000")
            )
        )

        // then
        verify {
            storage.setExpiring("key", "value", Instant.now(fixedClock).plusMillis(10_000))
        }

        confirmVerified(storage)
    }

    @Test
    fun `should set value with expiration time set to specific Unix seconds timestamp`() {
        // given
        every { storage.setExpiring(any(), any(), any()) } returns null

        // when
        command.execute(
            listOf(
                BulkStringData("SET"),
                BulkStringData("key"),
                BulkStringData("value"),
                BulkStringData("EXAT"),
                BulkStringData("10")
            )
        )

        // then
        verify {
            storage.setExpiring("key", "value", Instant.ofEpochSecond(10))
        }

        confirmVerified(storage)
    }

    @Test
    fun `should set value with expiration time set to specific Unix millis timestamp`() {
        // given
        every { storage.setExpiring(any(), any(), any()) } returns null

        // when
        command.execute(
            listOf(
                BulkStringData("SET"),
                BulkStringData("key"),
                BulkStringData("value"),
                BulkStringData("PXAT"),
                BulkStringData("10")
            )
        )

        // then
        verify {
            storage.setExpiring("key", "value", Instant.ofEpochMilli(10))
        }

        confirmVerified(storage)
    }

    @Test
    fun `should return error when number of arguments is not equal to 3`() {
        // when
        val result = command.execute(emptyList())

        // then
        assertIs<ErrorData>(result)
    }
}