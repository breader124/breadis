package com.breader.protocol.marshaler

import com.breader.protocol.type.ErrorData
import com.breader.protocol.type.SimpleStringData
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ErrorDataMarshallerTest {

    private val marshaller = ErrorDataMarshaller()

    @Test
    fun `should marshall error consisting from type and message when both are passed`() {
        assertEquals("-ERROR error message\r\n", marshaller.marshall(ErrorData("ERROR", "error message")))
    }

    @Test
    fun `should marshall error consisting from type when only type is passed`() {
        assertEquals("-ERROR\r\n", marshaller.marshall(ErrorData("ERROR", "")))
    }

    @Test
    fun `should throw IllegalArgumentException when data is not ErrorData`() {
        assertFailsWith<IllegalArgumentException> {
            marshaller.marshall(SimpleStringData("ERROR"))
        }
    }
}