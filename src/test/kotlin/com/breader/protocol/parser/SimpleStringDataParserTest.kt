package com.breader.protocol.parser

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class SimpleStringDataParserTest {

    private val dataParser = SimpleStringDataParser()

    @Test
    fun `should parse simple string message`() {
        assertEquals("OK", dataParser.parse("OK").value)
    }

    @Test
    fun `should not parse simple string message with multiple termination signs`() {
        assertFailsWith<IllegalArgumentException> { dataParser.parse("+OK\r\n") }
    }

    @Test
    fun `should not parse simple string message with single carriage return sign`() {
        assertFailsWith<IllegalArgumentException> { dataParser.parse("+OK\r") }
    }

    @Test
    fun `should not parse simple string message with single new line sign`() {
        assertFailsWith<IllegalArgumentException> { dataParser.parse("+OK\n") }
    }
}