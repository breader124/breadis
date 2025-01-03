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

class ExistsCommandTest {

    private val storage = mockk<Storage>()
    private val command = ExistsCommand(storage)

    @Test
    fun `should return error if no keys provided`() {
        assertIs<ErrorData>(command.execute(listOf(BulkStringData("EXISTS"))))
    }

    @Test
    fun `should return 0 if no keys exist`() {
        // given
        every { storage.exists(any()) } returns false

        // when
        val result = command.execute(
            listOf(
                BulkStringData("EXISTS"),
                BulkStringData("key")
            )
        )

        // then
        assertIs<IntegerData>(result)
        assertEquals(0, result.value)
    }

    @Test
    fun `should check key existence`() {
        // given
        every { storage.exists(any()) } returns true

        // when
        val result = command.execute(
            listOf(
                BulkStringData("EXISTS"),
                BulkStringData("key")
            )
        )

        // then
        assertIs<IntegerData>(result)
        assertEquals(1, result.value)
    }

    @Test
    fun `should check multiple keys existence`() {
        // given
        every { storage.exists(any()) } returnsMany listOf(true, false, true)

        // when
        val result = command.execute(
            listOf(
                BulkStringData("EXISTS"),
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