package com.breader.protocol.marshaler

import com.breader.protocol.type.BulkStringData
import com.breader.protocol.type.SimpleStringData
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class BulkStringDataMarshallerTest {

    private val marshaller = BulkStringDataMarshaller()

    @Test
    fun `should marshall bulk string`() {
        assertEquals("\$5\r\nhello\r\n", marshaller.marshall(BulkStringData("hello")))
    }

    @Test
    fun `should not marshall other data type`() {
        assertFailsWith<IllegalArgumentException> {
            marshaller.marshall(SimpleStringData("hello"))
        }
    }
}