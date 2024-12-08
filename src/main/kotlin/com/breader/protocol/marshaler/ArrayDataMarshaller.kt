package com.breader.protocol.marshaler

import com.breader.protocol.type.ArrayData
import com.breader.protocol.type.BulkStringData
import com.breader.protocol.type.Data
import com.breader.protocol.type.ErrorData
import com.breader.protocol.type.IntegerData
import com.breader.protocol.type.SimpleStringData
import kotlin.reflect.KClass

class ArrayDataMarshaller : DataMarshaller {

    private val marshallers: Map<KClass<out Data>, DataMarshaller> = mapOf(
        SimpleStringData::class to SimpleStringDataMarshaller(),
        IntegerData::class to IntegerDataMarshaller(),
        ErrorData::class to ErrorDataMarshaller(),
        BulkStringData::class to BulkStringDataMarshaller(),
        ArrayData::class to this
    )

    override fun marshall(data: Data): String {
        if (data !is ArrayData) {
            throw IllegalArgumentException("ArrayDataMarshaller can only marshall ArrayData")
        }

        return build {
            append("*${data.value.size}\r\n")
            data.value.forEach {
                val marshaller = marshallers[it::class] ?: throw IllegalArgumentException("Unsupported data type")
                append(marshaller.marshall(it))
            }
        }
    }

    private fun build(builder: StringBuilder.() -> Unit): String {
        val sb = StringBuilder()
        sb.builder()
        return sb.toString()
    }
}