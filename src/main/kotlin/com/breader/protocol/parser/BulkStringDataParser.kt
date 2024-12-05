package com.breader.protocol.parser

import com.breader.protocol.type.BulkStringData
import com.breader.protocol.type.Data
import com.breader.protocol.type.NullData

class BulkStringDataParser : DataParser {

    override fun parse(message: String): Data {
        if (message == "-1") {
            return NullData()
        }

        val length = runCatching { message.substringBefore("\r\n", "").toInt() }
            .getOrElse { throw IllegalArgumentException(it.message) }

        val content = message.substringAfter("\r\n").take(length)
        return BulkStringData(content)
    }
}