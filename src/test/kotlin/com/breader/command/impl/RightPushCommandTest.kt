package com.breader.command.impl

import com.breader.engine.Storage
import com.breader.protocol.type.BulkStringData
import com.breader.protocol.type.ErrorData
import com.breader.protocol.type.IntegerData
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class RightPushCommandTest {

    private val storage = mockk<Storage>()
    private val command = RightPushCommand(storage)

    @Test
    fun `should return error if no value provided`() {
        assertIs<ErrorData>(command.execute(listOf(BulkStringData("LPUSH"))))
    }

    @Test
    fun `should return error if only key is provided`() {
        assertIs<ErrorData>(command.execute(listOf(BulkStringData("LPUSH"), BulkStringData("key"))))
    }

    @Test
    fun `should return list size after pushing values`() {
        // when
        every { storage.rpush(any(), any()) } returns 3

        // when
        val result = command.execute(
            listOf(
                BulkStringData("LPUSH"),
                BulkStringData("key"),
                BulkStringData("value1"),
                BulkStringData("value2"),
                BulkStringData("value3")
            )
        )

        // then
        assertIs<IntegerData>(result)
        assertEquals(3, result.value)
    }

    @Test
    fun `should push only values omitting the key itself`() {
        // when
        every { storage.rpush(any(), any()) } returns 3

        // when
        val result = command.execute(
            listOf(
                BulkStringData("LPUSH"),
                BulkStringData("key"),
                BulkStringData("value1"),
                BulkStringData("value2"),
                BulkStringData("value3")
            )
        )

        // then
        verify { storage.rpush("key", listOf("value1", "value2", "value3")) }
        confirmVerified(storage)
    }

    @Test
    fun `should return error if storage throws exception`() {
        // when
        every { storage.rpush(any(), any()) } throws IllegalArgumentException("WRONGTYPE")

        // when
        val result = command.execute(
            listOf(
                BulkStringData("LPUSH"),
                BulkStringData("key"),
                BulkStringData("value1"),
                BulkStringData("value2"),
                BulkStringData("value3")
            )
        )

        // then
        assertIs<ErrorData>(result)
        assertEquals("WRONGTYPE", result.type)
    }
}