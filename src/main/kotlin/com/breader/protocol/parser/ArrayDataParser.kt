package com.breader.protocol.parser

import com.breader.protocol.type.ArrayData
import com.breader.protocol.type.Data

class ArrayDataParser : DataParser {

    companion object {
        const val TYPE_LENGTH = 1
        const val TERMINATOR_LENGTH = 2
    }

    private val primitiveTypeDataParsers = mapOf<Char, DataParser>(
        '+' to SimpleStringDataParser(),
        '-' to ErrorDataParser(),
        ':' to IntegerDataParser(),
        '$' to BulkStringDataParser()
    )

    // TODO support for nested arrays
    override fun parse(data: String): ArrayData {
        val arraySize = runCatching { data.substringBefore("\r\n", "").toInt() }
            .getOrElse { throw IllegalArgumentException(it.message) }

        val parsedArrayContent = mutableListOf<Data>()

        var rawArrayContent = data.substringAfter("\r\n")
        repeat (arraySize) {
            val type = rawArrayContent.first()
            val data = rawArrayContent.extractNextData(type)

            primitiveTypeDataParsers[type]?.parse(data)
                ?.let { parsedArrayContent.add(it) }
                ?: throw IllegalArgumentException("Unsupported data type: $data")

            rawArrayContent = rawArrayContent.substring(TYPE_LENGTH + data.length + TERMINATOR_LENGTH)
        }

        return ArrayData(parsedArrayContent)
    }

    private fun String.extractNextData(type: Char): String = when (type) {
        '+', '-', ':' -> substring(1).substringBefore("\r\n")
        '$' -> {
            val dataLength = substring(1).substringBefore("\r\n").toInt()
            val lengthPos = indexOf("\r\n")
            if (dataLength == -1) {
                substring(1, lengthPos)
            } else {
                substring(1, lengthPos + TERMINATOR_LENGTH + dataLength)
            }
        }
        else -> throw IllegalArgumentException("Unsupported data type: $type")
    }
}