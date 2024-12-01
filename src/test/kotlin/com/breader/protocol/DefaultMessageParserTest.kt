package com.breader.protocol

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class DefaultMessageParserTest {

    val messageParser = DefaultMessageParser()

    @Test
    fun `should not parse unsupported data type`() {
        assertThrows<IllegalArgumentException> { messageParser.parse("~OK\r\n") }
    }

    @Test
    fun `should not parse message without termination sign`() {
        assertThrows<IllegalArgumentException> { messageParser.parse("+OK") }
    }
}