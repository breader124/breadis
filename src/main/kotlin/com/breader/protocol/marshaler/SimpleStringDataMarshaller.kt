package com.breader.protocol.marshaler

import com.breader.protocol.type.Data
import com.breader.protocol.type.SimpleStringData

class SimpleStringDataMarshaller : DataMarshaller {
    override fun marshall(data: Data): String {
        if (data !is SimpleStringData) {
            throw IllegalArgumentException("SimpleStringDataMarshaller can only marshall SimpleStringData")
        }

        return "+${data.value}\r\n"
    }
}