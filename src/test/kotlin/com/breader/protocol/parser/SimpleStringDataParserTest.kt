package com.breader.protocol.parser

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class SimpleStringDataParserTest {

    private val messageParser = SimpleStringDataParser()

    @Test
    fun `should parse simple string message`() {
        assertEquals("OK", messageParser.parse("+OK\r\n").value)
    }

    @Test
    fun `should not parse simple string message with multiple termination signs`() {
        assertFailsWith<IllegalArgumentException> { messageParser.parse("+OK\r\n") }
    }

    @Test
    fun `should not parse simple string message with single carriage return sign`() {
        assertFailsWith<IllegalArgumentException> { messageParser.parse("+OK\r") }
    }

    @Test
    fun `should not parse simple string message with single new line sign`() {
        assertFailsWith<IllegalArgumentException> { messageParser.parse("+OK\n") }
    }
}