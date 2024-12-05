package com.breader.protocol.parser

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ErrorDataParserTest {

    private val dataParser = ErrorDataParser()

    @Test
    fun `should parse error message with details delimited by space`() {
        // when
        val message = dataParser.parse("ERR unknown command 'foobar'")

        // then
        assertEquals("ERR", message.type)
        assertEquals("unknown command 'foobar'", message.message)
    }

    @Test
    fun `should parse error message with details delimited by new line`() {
        // when
        val message = dataParser.parse("ERR\nunknown command 'foobar'")

        // then
        assertEquals("ERR", message.type)
        assertEquals("unknown command 'foobar'", message.message)
    }

    @Test
    fun `should parse only error type without details message`() {
        // when
        val message = dataParser.parse("ERR")

        // then
        assertEquals("ERR", message.type)
        assertTrue(message.message.isEmpty())
    }
}