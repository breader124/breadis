package com.breader.protocol

import com.breader.protocol.parser.MessageParser
import com.breader.protocol.parser.SimpleStringDataParser
import com.breader.protocol.type.Data

class DefaultMessageParser : MessageParser {

    private val specializedParsers = mapOf<Char, MessageParser>(
        '+' to SimpleStringDataParser()
    )

    override fun parse(rawMessage: String): Data {
        val dataType = rawMessage.first()
        specializedParsers[dataType]
            ?.let { return it.parse(rawMessage) }
            ?: throw IllegalArgumentException("Unsupported data type")
    }
}