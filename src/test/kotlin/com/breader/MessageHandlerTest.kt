package com.breader

import com.breader.engine.Storage
import kotlin.test.Test
import kotlin.test.assertEquals

class MessageHandlerTest {

    private val storage = Storage()
    private val messageHandler = MessageHandler(storage)

    @Test
    fun `should handle message`() {
        assertEquals("+PONG\r\n", messageHandler.handle("*1\r\n$4\r\nPING\r\n"))
    }

    @Test
    fun `should handle handling error`() {
        assertEquals("-ERR Unknown command\r\n", messageHandler.handle("*1\r\n$2\r\nHI\r\n"))
    }
}