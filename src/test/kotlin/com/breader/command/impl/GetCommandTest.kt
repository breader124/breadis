package com.breader.command.impl

import com.breader.engine.Storage
import com.breader.engine.data.InternalData
import com.breader.engine.data.InternalString
import com.breader.protocol.type.BulkStringData
import com.breader.protocol.type.ErrorData
import com.breader.protocol.type.NullData
import io.mockk.every
import io.mockk.mockk
import kotlin.test.Test
import kotlin.test.assertIs

class GetCommandTest {

    private val storage = mockk<Storage>()
    private val command = GetCommand(storage)

    @Test
    fun `should return value`() {
        // given
        every { storage.get("key") } returns InternalString("value")

        val args = listOf(BulkStringData("GET"), BulkStringData("key"))

        // when
        val result = command.execute(args)

        // then
        assertIs<BulkStringData>(result)
    }

    @Test
    fun `should return null when value is not found`() {
        // given
        every { storage.get("key") } returns null

        val args = listOf(BulkStringData("GET"), BulkStringData("key"))

        // when
        val result = command.execute(args)

        // then
        assertIs<NullData>(result)
    }

    @Test
    fun `should return error when value is not of a string type`() {
        // given
        every { storage.get("key") } returns InternalData()

        val args = listOf(BulkStringData("GET"), BulkStringData("key"))

        // when
        val result = command.execute(args)

        // then
        assertIs<ErrorData>(result)
    }

    @Test
    fun `should return error when number of arguments is not equal to 2`() {
        // given
        val args = emptyList<BulkStringData>()

        // when
        val result = command.execute(args)

        // then
        assertIs<ErrorData>(result)
    }
}