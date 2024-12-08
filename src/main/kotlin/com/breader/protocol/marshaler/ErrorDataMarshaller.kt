package com.breader.protocol.marshaler

import com.breader.protocol.type.Data
import com.breader.protocol.type.ErrorData

class ErrorDataMarshaller : DataMarshaller {
    override fun marshall(data: Data): String {
        if (data !is ErrorData) {
            throw IllegalArgumentException("ErrorDataMarshaller can only marshall ErrorData")
        }

        return if (data.message.isBlank()) {
            "-${data.type}\r\n"
        } else {
            "-${data.type} ${data.message}\r\n"
        }
    }
}