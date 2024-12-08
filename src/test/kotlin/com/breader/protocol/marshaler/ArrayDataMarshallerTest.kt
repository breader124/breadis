package com.breader.protocol.marshaler

import com.breader.protocol.type.ArrayData
import com.breader.protocol.type.SimpleStringData
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ArrayDataMarshallerTest {

    private val marshaller = ArrayDataMarshaller()

    @Test
    fun `should marshall array data`() {
        // given
        val data = ArrayData(listOf(SimpleStringData("test1"), SimpleStringData("test2")))

        // when
        val marshalled = marshaller.marshall(data)

        // then
        assertEquals("*2\r\n+test1\r\n+test2\r\n", marshalled)
    }

    @Test
    fun `should marshall empty array data`() {
        // given
        val data = ArrayData(emptyList())

        // when
        val marshalled = marshaller.marshall(data)

        // then
        assertEquals("*0\r\n", marshalled)
    }

    @Test
    fun `should marshall nested array data`() {
        // given
        val data = ArrayData(listOf(ArrayData(listOf(SimpleStringData("test1"), SimpleStringData("test2")))))

        // when
        val marshalled = marshaller.marshall(data)

        // then
        assertEquals("*1\r\n*2\r\n+test1\r\n+test2\r\n", marshalled)
    }

    @Test
    fun `should throw IllegalArgumentException when data is not ArrayData`() {
        assertFailsWith<IllegalArgumentException> {
            marshaller.marshall(SimpleStringData("test"))
        }
    }
}