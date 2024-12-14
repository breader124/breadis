package com.breader

import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.server.application.*
import io.ktor.utils.io.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun Application.configureSockets() {
    val selectorManager = ActorSelectorManager(Dispatchers.IO)
    val defaultPort = 9002

    val messageHandler = MessageHandler()

    runBlocking {
        aSocket(selectorManager).tcp().bind(port = defaultPort).use {
            while (true) {
                val socket = it.accept()
                launch {
                    val read = socket.openReadChannel()
                    val write = socket.openWriteChannel(autoFlush = true)
                    runCatching {
                        while (true) {
                            handleIncomingMessage(read, messageHandler, write)
                        }
                    }.onFailure {
                        socket.close()
                    }
                }
            }
        }
    }
}

private suspend fun handleIncomingMessage(
    read: ByteReadChannel,
    messageHandler: MessageHandler,
    write: ByteWriteChannel
) {
    val message = ByteArray(8192).let {
        read.readAvailable(it)
        it.filterNot { byte -> byte.toInt() == 0 }
            .toByteArray()
            .let { bytes -> String(bytes, Charsets.UTF_8) }
    }
    val result = message.let { messageHandler.handle(it) }
    write.writeStringUtf8(result)
}
