package com.breader.protocol.parser

import com.breader.protocol.type.Data

interface DataParser {
    fun parse(message: String): Data
}

