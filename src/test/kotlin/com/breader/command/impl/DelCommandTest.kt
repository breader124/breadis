package com.breader.command.impl

import com.breader.engine.Storage
import com.breader.protocol.type.BulkStringData
import com.breader.protocol.type.ErrorData
import com.breader.protocol.type.IntegerData
import io.mockk.every
import io.mockk.mockk
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class DelCommandTest {

    private val storage = mockk<Storage>()
    private val command = DelCommand(storage)

    @Test
    fun `should return error if no keys provided`() {
        assertIs<ErrorData>(command.execute(listOf(BulkStringData("DEL"))))
    }

    @Test
    fun `should return 0 if no keys exist`() {
        // given
        every { storage.delete(any()) } returns false

        // when
        val result = command.execute(
            listOf(
                BulkStringData("DEL"),
                BulkStringData("key")
            )
        )

        // then
        assertIs<IntegerData>(result)
        assertEquals(0, result.value)
    }

    @Test
    fun `should delete a key`() {
        // given
        every { storage.delete(any()) } returns true

        // when
        val result = command.execute(
            listOf(
                BulkStringData("DEL"),
                BulkStringData("key")
            )
        )

        // then
        assertIs<IntegerData>(result)
        assertEquals(1, result.value)
    }

    @Test
    fun `should delete multiple keys`() {
        // given
        every { storage.delete(any()) } returnsMany listOf(true, true, false)

        // when
        val result = command.execute(
            listOf(
                BulkStringData("DEL"),
                BulkStringData("key1"),
                BulkStringData("key2"),
                BulkStringData("key3")
            )
        )

        // then
        assertIs<IntegerData>(result)
        assertEquals(2, result.value)
    }
}