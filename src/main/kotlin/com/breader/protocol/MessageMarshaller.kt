package com.breader.protocol

import com.breader.protocol.marshaler.ArrayDataMarshaller
import com.breader.protocol.marshaler.BulkStringDataMarshaller
import com.breader.protocol.marshaler.DataMarshaller
import com.breader.protocol.marshaler.ErrorDataMarshaller
import com.breader.protocol.marshaler.IntegerDataMarshaller
import com.breader.protocol.marshaler.SimpleStringDataMarshaller
import com.breader.protocol.type.ArrayData
import com.breader.protocol.type.BulkStringData
import com.breader.protocol.type.Data
import com.breader.protocol.type.ErrorData
import com.breader.protocol.type.IntegerData
import com.breader.protocol.type.NullData
import com.breader.protocol.type.SimpleStringData
import kotlin.reflect.KClass

class MessageMarshaller {

    private val specializedMarshallers = mapOf<KClass<out Data>, DataMarshaller>(
        SimpleStringData::class to SimpleStringDataMarshaller(),
        IntegerData::class to IntegerDataMarshaller(),
        ErrorData::class to ErrorDataMarshaller(),
        BulkStringData::class to BulkStringDataMarshaller(),
        NullData::class to BulkStringDataMarshaller(),
        ArrayData::class to ArrayDataMarshaller()
    )

    fun marshall(data: Data): String {
        return specializedMarshallers[data::class]?.marshall(data) ?: throw IllegalArgumentException("Unsupported data type")
    }
}