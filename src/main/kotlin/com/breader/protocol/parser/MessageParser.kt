package com.breader.protocol.parser

import com.breader.protocol.type.Data
import com.breader.protocol.type.SimpleStringData

interface MessageParser {
    fun parse(message: String): Data
}

class SimpleStringDataParser : MessageParser {

    override fun parse(message: String): Data {
        val endMessagePos = message.indexOf("\r\n")
        if (noTerminationSign(endMessagePos) || multipleTerminationSigns(endMessagePos, message)) {
            throw IllegalArgumentException("Invalid message format")
        }

        val extractedMessage = message.substring(1, endMessagePos)
        if (extractedMessage.contains("\r") || extractedMessage.contains("\n")) {
            throw IllegalArgumentException("Invalid message format")
        }

        return SimpleStringData(extractedMessage)
    }

    private fun noTerminationSign(endMessagePos: Int): Boolean = endMessagePos == -1

    private fun multipleTerminationSigns(endMessagePos: Int, message: String): Boolean = endMessagePos != message.lastIndexOf("\r\n")
}