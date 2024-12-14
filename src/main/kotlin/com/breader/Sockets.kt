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
                        val line = read.readUTF8Line()
                        // parse the received message
                        // TODO try to recognize the command (technically, it's an array of bulk strings)
                        // execute the command (commands use data types defined by the protocol)
                        // marshall the result (commands result is marshalled according to the protocol)
                        // send the result back to the client
                        write.writeStringUtf8("$line\n")
                    }
                } catch (_: Throwable) {
                    socket.close()
                }
            }
        }
    }
}
