package com.breader.protocol.parser

import com.breader.protocol.type.BulkStringData
import com.breader.protocol.type.NullData
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class BulkStringDataParserTest {

    private val messageParser = BulkStringDataParser()

    @Test
    fun `should parse bulk string`() {
        val message = messageParser.parse("5\r\nhello")

        assertTrue(message is BulkStringData)
        assertEquals("hello", message.value)
    }

    @Test
    fun `should parse empty bulk string`() {
        val message = messageParser.parse("0\r\n")

        assertTrue(message is BulkStringData)
        assertEquals("", message.value)
    }

    @Test
    fun `should parse bulk string with special signs`() {
        val message = messageParser.parse("12\r\nhello\r\nworld")

        assertTrue(message is BulkStringData)
        assertEquals("hello\r\nworld", message.value)
    }

    @Test
    fun `should parse null bulk string`() {
        assertTrue(messageParser.parse("-1\r\n") is NullData)
    }

    @Test
    fun `should not parse bulk string with invalid length`() {
        assertFailsWith<IllegalArgumentException> { messageParser.parse("a\r\nhello") }
    }
}