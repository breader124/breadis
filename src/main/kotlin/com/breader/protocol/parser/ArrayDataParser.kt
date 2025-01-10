package com.breader.protocol.parser

import com.breader.protocol.type.ArrayData
import com.breader.protocol.type.Data

private const val TYPE_LENGTH = 1
private const val TERMINATOR = "\r\n"

class ArrayDataParser : DataParser {

    private val primitiveTypeDataParsers = mapOf<Char, DataParser>(
        '+' to SimpleStringDataParser(),
        '-' to ErrorDataParser(),
        ':' to IntegerDataParser(),
        '$' to BulkStringDataParser()
    )

    override fun parse(data: String): ArrayData = internalParse(data).first

    private fun internalParse(data: String): Pair<ArrayData, String> {
        if (data == "0") {
            return ArrayData(emptyList()) to ""
        }

        var (arraySize, contentToParse) = decomposeArray(data)

        val parsedContent = mutableListOf<Data>()
        repeat(arraySize) {
            val type = contentToParse.first()
            contentToParse = if (primitiveTypeDataParsers.keys.contains(type)) {
                parsePrimitiveTypeData(type, parsedContent, contentToParse)
            } else {
                parseArray(contentToParse, parsedContent)
            }
        }

        return ArrayData(parsedContent) to contentToParse
    }

    private fun decomposeArray(data: String): Pair<Int, String> {
        val arraySize = runCatching { data.substringBefore(TERMINATOR, "").toInt() }
            .getOrElse { throw IllegalArgumentException(it.message) }
        var contentToParse = data.substringAfter(TERMINATOR)

        return arraySize to contentToParse
    }

    private fun parsePrimitiveTypeData(type: Char, parsedContent: MutableList<Data>, contentToParse: String): String {
        val data = contentToParse.extractNextData(type)
        primitiveTypeDataParsers[type]?.parse(data)
            ?.let { parsedContent.add(it) }
            ?: throw IllegalArgumentException("Unsupported data type: $data")

        var newContent = contentToParse.substring(TYPE_LENGTH + data.length)
        if (newContent.isNotEmpty()) {
            newContent = newContent.substring(TERMINATOR.length)
        }

        return newContent
    }

    private fun parseArray(contentToParse: String, parsedContent: MutableList<Data>): String {
        val arrayWithoutType = contentToParse.substring(1)
        val (parsedNestedArray, contentWithoutNestedArray) = internalParse(arrayWithoutType)
        parsedContent.add(parsedNestedArray)

        return contentWithoutNestedArray
    }

    private fun String.extractNextData(type: Char): String = when (type) {
        '+', '-', ':' -> substring(1).substringBefore(TERMINATOR)
        '$' -> {
            val dataLength = substring(1).substringBefore(TERMINATOR).toInt()
            if (dataLength == -1) {
                substring(1)
            } else {
                val lengthPos = indexOf(TERMINATOR)
                substring(1, lengthPos + TERMINATOR.length + dataLength)
            }
        }

        else -> throw IllegalArgumentException("Unsupported data type: $type")
    }
}