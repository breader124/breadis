package com.breader.protocol.marshaler

import com.breader.protocol.type.IntegerData
import com.breader.protocol.type.SimpleStringData
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class IntegerDataMarshallerTest {

    private val marshaller = IntegerDataMarshaller()

    @Test
    fun `should marshall integer`() {
        assertEquals(":5\r\n", marshaller.marshall(IntegerData(5)))
    }

    @Test
    fun `should not marshall other data type`() {
        assertFailsWith<IllegalArgumentException> {
            marshaller.marshall(SimpleStringData("hello"))
        }
    }
}