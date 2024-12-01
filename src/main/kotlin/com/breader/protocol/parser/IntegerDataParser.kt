package com.breader.protocol.parser

import com.breader.protocol.type.IntegerData

class IntegerDataParser : MessageParser {

    override fun parse(message: String): IntegerData = runCatching { message.toInt() }
        .getOrElse { throw IllegalArgumentException(it.message) }
        .let { IntegerData(it) }
}