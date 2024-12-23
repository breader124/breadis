package com.breader.command.impl

import com.breader.engine.Storage
import com.breader.protocol.type.BulkStringData
import com.breader.protocol.type.ErrorData
import com.breader.protocol.type.SimpleStringData
import io.mockk.every
import io.mockk.mockk
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class SetCommandTest {

    private val storage = mockk<Storage>()
    private val command = SetCommand(storage)

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
    fun `should return error when number of arguments is not equal to 3`() {
        // when
        val result = command.execute(emptyList())

        // then
        assertIs<ErrorData>(result)
    }
}