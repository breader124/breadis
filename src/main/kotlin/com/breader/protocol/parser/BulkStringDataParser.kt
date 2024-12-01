package com.breader.protocol.parser

import com.breader.protocol.type.BulkStringData
import com.breader.protocol.type.Data
import com.breader.protocol.type.NullData

class BulkStringDataParser : MessageParser {

    override fun parse(message: String): Data {
        val length = runCatching { message.substringBefore("\r\n", "").toInt() }
            .getOrElse { throw IllegalArgumentException(it.message) }

        if (length == -1) {
            return NullData()
        }

        val content = message.substringAfter("\r\n").take(length)
        return BulkStringData(content)
    }
}