package com.breader.command.impl

import com.breader.engine.Storage
import com.breader.protocol.type.BulkStringData
import com.breader.protocol.type.Data
import com.breader.protocol.type.ErrorData
import com.breader.protocol.type.IntegerData

class ExistsCommand(private val storage: Storage) : Command {

    override fun name(): BulkStringData = BulkStringData("EXISTS")

    override fun execute(args: List<BulkStringData>): Data {
        if (args.size < 2) {
            return ErrorData("ARG", "EXISTS command requires at least one key")
        }

        val keys = args.drop(1)
        return keys.map { storage.exists(it.value) }
            .count { it }
            .let { IntegerData(it) }
    }
}