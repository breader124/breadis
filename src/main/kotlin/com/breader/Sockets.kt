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
        val serverSocket = aSocket(selectorManager).tcp().bind(port = defaultPort)
        println("Echo Server listening at ${serverSocket.localAddress}")
        while (true) {
            val socket = serverSocket.accept()
            println("Accepted $socket")
            launch {
                val read = socket.openReadChannel()
                val write = socket.openWriteChannel(autoFlush = true)
                try {
                    while (true) {
                        val message = ByteArray(8192).let {
                            read.readAvailable(it)
                            it.filterNot { byte -> byte.toInt() == 0 }
                                .toByteArray()
                                .let { bytes -> String(bytes, Charsets.UTF_8) }
                        }
                        val result = message.let { messageHandler.handle(it) }
                        write.writeStringUtf8(result)
                    }
                } catch (_: Throwable) {
                    socket.close()
                }
            }
        }
    }
}
