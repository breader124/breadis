package com.breader.protocol.parser

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class SimpleStringDataParserTest {

    private val messageParser = SimpleStringDataParser()

    @Test
    fun `should parse simple string message`() {
        assertEquals("OK", messageParser.parse("+OK\r\n").value)
    }

    @Test
    fun `should not parse simple string message with multiple termination signs`() {
        assertThrows<IllegalArgumentException> { messageParser.parse("+OK\r\n") }
    }

    @Test
    fun `should not parse simple string message with single carriage return sign`() {
        assertThrows<IllegalArgumentException> { messageParser.parse("+OK\r") }
    }

    @Test
    fun `should not parse simple string message with single new line sign`() {
        assertThrows<IllegalArgumentException> { messageParser.parse("+OK\n") }
    }
}