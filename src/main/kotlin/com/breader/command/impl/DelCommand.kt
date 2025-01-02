package com.breader.command.impl

import com.breader.engine.Storage
import com.breader.protocol.type.BulkStringData
import com.breader.protocol.type.Data
import com.breader.protocol.type.ErrorData
import com.breader.protocol.type.IntegerData

class DelCommand(private val storage: Storage) : Command {

    override fun name() = BulkStringData("DEL")

    override fun execute(args: List<BulkStringData>): Data {
        if (args.size < 2) {
            return ErrorData("ARG", "DEL command requires at least one key")
        }

        val keys = args.drop(1)
        return keys.map { storage.delete(it.value) }
            .count { it }
            .let { IntegerData(it) }
    }
}