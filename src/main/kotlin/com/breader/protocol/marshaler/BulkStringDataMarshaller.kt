package com.breader.protocol.marshaler

import com.breader.protocol.type.BulkStringData
import com.breader.protocol.type.Data

class BulkStringDataMarshaller : DataMarshaller {
    override fun marshall(data: Data): String {
        if (data !is BulkStringData) {
            throw IllegalArgumentException("BulkStringDataMarshaller can only marshall BulkStringData")
        }

        return "\$${data.value.length}\r\n${data.value}\r\n"
    }
}