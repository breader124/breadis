package com.breader

import com.breader.command.CommandDispatcher
import com.breader.protocol.MessageMarshaller
import com.breader.protocol.MessageParser

class MessageHandler() {

    private val messageParser = MessageParser()
    private val commandDispatcher = CommandDispatcher()
    private val messageMarshaller = MessageMarshaller()

    fun handle(message: String): String {
        val parsedMessage = messageParser.parse(message)
        val result = commandDispatcher.dispatch(parsedMessage)
        return messageMarshaller.marshall(result)
    }
}