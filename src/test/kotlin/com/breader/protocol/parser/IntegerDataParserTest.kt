package com.breader.protocol.parser

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class IntegerDataParserTest {

    private val messageParser = IntegerDataParser()

    @Test
    fun `should parse integer`() {
        assertEquals(123, messageParser.parse("123").value)
    }

    @Test
    fun `should parse negative integer`() {
        assertEquals(-123, messageParser.parse("-123").value)
    }

    @Test
    fun `should throw exception when parsing non-integer value`() {
        assertFailsWith<IllegalArgumentException> {
            messageParser.parse("abc")
        }
    }
}