package com.breader.protocol.marshaler

import com.breader.protocol.type.Data

interface DataMarshaller {
    fun marshall(data: Data): String
}