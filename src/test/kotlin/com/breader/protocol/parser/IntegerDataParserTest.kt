package com.breader.protocol.parser

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class IntegerDataParserTest {

    private val dataParser = IntegerDataParser()

    @Test
    fun `should parse integer`() {
        assertEquals(123, dataParser.parse("123").value)
    }

    @Test
    fun `should parse negative integer`() {
        assertEquals(-123, dataParser.parse("-123").value)
    }

    @Test
    fun `should throw exception when parsing non-integer value`() {
        assertFailsWith<IllegalArgumentException> {
            dataParser.parse("abc")
        }
    }
}