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
        val terminationSignPos = rawMessage.lastIndexOf("\r\n").let {
            if (it == -1) throw IllegalArgumentException("No termination sign") else it
        }

        specializedParsers[dataType]
            ?.let { return it.parse(rawMessage.substring(1, terminationSignPos)) }
            ?: throw IllegalArgumentException("Unsupported data type")
    }
}