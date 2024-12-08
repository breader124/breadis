package com.breader.protocol.parser

import com.breader.protocol.type.ArrayData
import com.breader.protocol.type.Data

class ArrayDataParser : DataParser {

    companion object {
        private const val TYPE_LENGTH = 1
        private const val TERMINATOR = "\r\n"
    }

    private val primitiveTypeDataParsers = mapOf<Char, DataParser>(
        '+' to SimpleStringDataParser(),
        '-' to ErrorDataParser(),
        ':' to IntegerDataParser(),
        '$' to BulkStringDataParser()
    )

    override fun parse(data: String): ArrayData = internalParse(data).first

    private fun internalParse(data: String): Pair<ArrayData, String> {
        val arraySize = runCatching { data.substringBefore(TERMINATOR, "").toInt() }
            .getOrElse { throw IllegalArgumentException(it.message) }
        var contentToParse = data.substringAfter(TERMINATOR)

        val parsedContent = mutableListOf<Data>()
        repeat(arraySize) {
            val type = contentToParse.first()

            if (primitiveTypeDataParsers.keys.contains(type)) {
                val data = contentToParse.extractNextData(type)

                primitiveTypeDataParsers[type]?.parse(data)
                    ?.let { parsedContent.add(it) }
                    ?: throw IllegalArgumentException("Unsupported data type: $data")

                contentToParse = contentToParse.substring(TYPE_LENGTH + data.length + TERMINATOR.length)
            } else {
                val arrayWithoutType = contentToParse.substring(1)
                val (parsedNestedArray, contentWithoutNestedArray) = internalParse(arrayWithoutType)

                parsedContent.add(parsedNestedArray)
                contentToParse = contentWithoutNestedArray
            }
        }

        return Pair(ArrayData(parsedContent), contentToParse)
    }

    private fun String.extractNextData(type: Char): String = when (type) {
        '+', '-', ':' -> substring(1).substringBefore(TERMINATOR)
        '$' -> {
            val dataLength = substring(1).substringBefore(TERMINATOR).toInt()
            val lengthPos = indexOf(TERMINATOR)
            if (dataLength == -1) {
                substring(1, lengthPos)
            } else {
                substring(1, lengthPos + TERMINATOR.length + dataLength)
            }
        }

        else -> throw IllegalArgumentException("Unsupported data type: $type")
    }
}