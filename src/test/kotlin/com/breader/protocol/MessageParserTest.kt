package com.breader.protocol

import com.breader.protocol.type.SimpleStringData
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class MessageParserTest {

    val messageParser = MessageParser()

    @Test
    fun `should parse message`() {
        val parsedMessage = messageParser.parse("+OK\r\n")

        assertTrue(parsedMessage is SimpleStringData)
        assertEquals("OK", parsedMessage.value)
    }

    @Test
    fun `should not parse unsupported data type`() {
        assertFailsWith<IllegalArgumentException> { messageParser.parse("~OK\r\n") }
    }

    @Test
    fun `should not parse message without termination sign`() {
        assertFailsWith<IllegalArgumentException> { messageParser.parse("+OK") }
    }
}