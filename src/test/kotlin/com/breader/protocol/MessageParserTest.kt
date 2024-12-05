package com.breader.protocol

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class MessageParserTest {

    val messageParser = MessageParser()

    @Test
    fun `should not parse unsupported data type`() {
        assertThrows<IllegalArgumentException> { messageParser.parse("~OK\r\n") }
    }

    @Test
    fun `should not parse message without termination sign`() {
        assertThrows<IllegalArgumentException> { messageParser.parse("+OK") }
    }
}