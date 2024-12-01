package com.breader.protocol.parser

import com.breader.protocol.type.SimpleStringData

class SimpleStringDataParser : MessageParser {

    override fun parse(message: String): SimpleStringData {
        if (message.contains("\r") || message.contains("\n")) {
            throw IllegalArgumentException("Invalid message format")
        }

        return SimpleStringData(message)
    }
}