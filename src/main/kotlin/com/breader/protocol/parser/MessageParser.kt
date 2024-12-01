package com.breader.protocol.parser

import com.breader.protocol.type.Data

interface MessageParser {
    fun parse(message: String): Data
}

