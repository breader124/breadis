package com.breader.protocol.parser

import com.breader.protocol.type.ArrayData
import com.breader.protocol.type.BulkStringData
import com.breader.protocol.type.IntegerData
import com.breader.protocol.type.NullData
import com.breader.protocol.type.SimpleStringData
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ArrayDataParserTest {

    private val dataParser = ArrayDataParser()

    @Test
    fun `should parse array consisting of same type elems`() {
        val message = dataParser.parse("3\r\n:1\r\n:2\r\n:3\r\n")

        assertEquals(3, message.value.size)
        message.value.forEachIndexed { index, data ->
            assertTrue(data is IntegerData)
            assertEquals(index + 1, data.value)
        }
    }

    @Test
    fun `should parse array consisting of different type elems`() {
        val message = dataParser.parse("3\r\n:1\r\n+OK\r\n$5\r\nhello\r\n")

        assertEquals(3, message.value.size)

        assertTrue(message.value[0] is IntegerData)
        assertEquals(1, (message.value[0] as IntegerData).value)

        assertTrue(message.value[1] is SimpleStringData)
        assertEquals("OK", (message.value[1] as SimpleStringData).value)

        assertTrue(message.value[2] is BulkStringData)
        assertEquals("hello", (message.value[2] as BulkStringData).value)
    }

    @Test
    fun `should parse empty array`() {
        val message = dataParser.parse("0\r\n")

        assertEquals(0, message.value.size)
    }

    @Test
    fun `should parse array with null elem`() {
        val message = dataParser.parse("1\r\n$-1\r\n")

        assertEquals(1, message.value.size)
        assertTrue(message.value[0] is NullData)
    }

    @Test
    fun `should parse array with nested array`() {
        val message = dataParser.parse("2\r\n*3\r\n:1\r\n:2\r\n:3\r\n*2\r\n+OK\r\n$5\r\nhello\r\n")

        assertEquals(2, message.value.size)
        with(message.value[0] as ArrayData) {
            for (i in 0 until 3) {
                assertTrue(value[i] is IntegerData)
            }
        }
        with(message.value[1] as ArrayData) {
            assertTrue(value[0] is SimpleStringData)
            assertTrue(value[1] is BulkStringData)
        }
    }

    @Test
    fun `should parse multi-level nested array`() {
        val message = dataParser.parse("1\r\n*1\r\n*1\r\n:1\r\n")

        assertEquals(1, message.value.size)
        with(message.value[0] as ArrayData) {
            assertEquals(1, value.size)
            with(value[0] as ArrayData) {
                assertEquals(1, value.size)
                assertTrue(value[0] is IntegerData)
            }
        }
    }
}