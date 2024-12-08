package com.breader.protocol.marshaler

import com.breader.protocol.type.IntegerData
import com.breader.protocol.type.SimpleStringData
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class SimpleStringDataMarshallerTest {

    private val marshaller = SimpleStringDataMarshaller()

    @Test
    fun `should marshall simple string`() {
        assertEquals("+hello\r\n", marshaller.marshall(SimpleStringData("hello")))
    }

    @Test
    fun `should not marshall other data type`() {
        assertFailsWith<IllegalArgumentException> {
            marshaller.marshall(IntegerData(5))
        }
    }
}