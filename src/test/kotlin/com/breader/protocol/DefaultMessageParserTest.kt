package com.breader.protocol

import com.breader.protocol.type.SimpleStringData
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertTrue

class DefaultMessageParserTest {

    val messageParser = DefaultMessageParser()

    @Test
    fun `should not parse unsupported data type`() {
        assertThrows<IllegalArgumentException> { messageParser.parse("~OK\r\n") }
    }

    @Test
    fun `should parse simple string message`() {
        // when
        val message = messageParser.parse("+OK\r\n")

        // then
        assertTrue(message is SimpleStringData)
        assertTrue(message.value == "OK")
    }

    @Test
    fun `should not parse simple string message with no termination sign`() {
        assertThrows<IllegalArgumentException> { messageParser.parse("+OK") }
    }

    @Test
    fun `should not parse simple string message with multiple termination signs`() {
        assertThrows<IllegalArgumentException> { messageParser.parse("+OK\r\nOK\r\n") }
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