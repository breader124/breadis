package com.breader

import com.breader.command.CommandDispatcher
import com.breader.engine.Storage
import com.breader.protocol.MessageMarshaller
import com.breader.protocol.MessageParser
import com.breader.protocol.type.ErrorData

class MessageHandler(storage: Storage) {

    private val messageParser = MessageParser()
    private val commandDispatcher = CommandDispatcher(storage)
    private val messageMarshaller = MessageMarshaller()

    fun handle(message: String): String = runCatching {
        val parsedMessage = messageParser.parse(message)
        val result = commandDispatcher.dispatch(parsedMessage)
        messageMarshaller.marshall(result)
    }.getOrElse {
        val error = ErrorData("ERR", it.message ?: "Unknown error")
        messageMarshaller.marshall(error)
    }
}