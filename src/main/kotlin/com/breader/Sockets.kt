package com.breader

import com.breader.engine.Storage
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

    val storage = Storage()
    val messageHandler = MessageHandler(storage)

    runBlocking(Dispatchers.Default) {
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
    val buffer = ByteArray(1024)
    val bytesRead = read.readAvailable(buffer).takeIf { it > 0 }
        ?: throw IllegalStateException("Socket already closed")

    val message = String(buffer, 0, bytesRead, Charsets.UTF_8)
    val response = messageHandler.handle(message)

    write.writeStringUtf8(response)
}
