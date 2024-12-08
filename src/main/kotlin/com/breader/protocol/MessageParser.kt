package com.breader.protocol

import com.breader.protocol.parser.ArrayDataParser
import com.breader.protocol.parser.BulkStringDataParser
import com.breader.protocol.parser.DataParser
import com.breader.protocol.parser.ErrorDataParser
import com.breader.protocol.parser.IntegerDataParser
import com.breader.protocol.parser.SimpleStringDataParser
import com.breader.protocol.type.Data

class MessageParser {

    private val specializedParsers = mapOf<Char, DataParser>(
        '+' to SimpleStringDataParser(),
        '-' to ErrorDataParser(),
        ':' to IntegerDataParser(),
        '$' to BulkStringDataParser(),
        '*' to ArrayDataParser()
    )

    fun parse(rawMessage: String): Data {
        val dataType = rawMessage.first()
        val terminationSignPos = rawMessage.lastIndexOf("\r\n").let {
            if (it == -1) throw IllegalArgumentException("No termination sign") else it
        }

        specializedParsers[dataType]
            ?.let { return it.parse(rawMessage.substring(1, terminationSignPos)) }
            ?: throw IllegalArgumentException("Unsupported data type")
    }
}