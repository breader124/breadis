package com.breader.protocol.parser

import com.breader.protocol.type.SimpleStringData

class SimpleStringDataParser : DataParser {
    override fun parse(message: String): SimpleStringData {
        require(!message.contains("\r") && !message.contains("\n")) { "Invalid message format" }
        return SimpleStringData(message)
    }
}