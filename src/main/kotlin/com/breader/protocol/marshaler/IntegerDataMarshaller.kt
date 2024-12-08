package com.breader.protocol.marshaler

import com.breader.protocol.type.Data
import com.breader.protocol.type.IntegerData

class IntegerDataMarshaller : DataMarshaller {
    override fun marshall(data: Data): String {
        if (data !is IntegerData) {
            throw IllegalArgumentException("IntegerDataMarshaller can only marshall IntegerData")
        }

        return ":${data.value}\r\n"
    }
}