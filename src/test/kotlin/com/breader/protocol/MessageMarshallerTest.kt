package com.breader.protocol

import com.breader.protocol.type.Data
import com.breader.protocol.type.NullData
import com.breader.protocol.type.SimpleStringData
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class MessageMarshallerTest {

    val messageMarshaller = MessageMarshaller()

    @Test
    fun `should marshall message`() {
        assertEquals("+OK\r\n", messageMarshaller.marshall(SimpleStringData("OK")))
    }

    @Test
    fun `should marshall null data`() {
        assertEquals("$-1\r\n", messageMarshaller.marshall(NullData()))
    }

    @Test
    fun `should not marshall message of unsupported type`() {
        assertFailsWith<IllegalArgumentException> {
            messageMarshaller.marshall(object : Data {})
        }
    }
}