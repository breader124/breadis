package com.breader.protocol.parser

import com.breader.protocol.type.ErrorData

class ErrorDataParser : MessageParser {

    override fun parse(message: String): ErrorData {
        val messageParts = message.split(' ', '\n')
        if (messageParts.size == 1) {
            return ErrorData(messageParts[0], "")
        } else {
            val details = message.substring(messageParts[0].length + 1)
            return ErrorData(messageParts[0], details)
        }
    }
}