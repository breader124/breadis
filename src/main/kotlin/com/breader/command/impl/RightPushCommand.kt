package com.breader.command.impl

import com.breader.engine.Storage
import com.breader.protocol.type.BulkStringData
import com.breader.protocol.type.Data
import com.breader.protocol.type.ErrorData
import com.breader.protocol.type.IntegerData

class RightPushCommand(private val storage: Storage) : Command {

    override fun name() = BulkStringData("RPUSH")

    override fun execute(args: List<BulkStringData>): Data {
        if (args.size < 3) {
            return ErrorData("ARG", "RPUSH command requires at least one value to be pushed")
        }

        val values = args.drop(2)
        return runCatching { storage.rpush(args[1].value, values.map { it.value }) }
            .fold(
                onSuccess = { IntegerData(it) },
                onFailure = {
                    if (it is IllegalArgumentException) {
                        ErrorData("WRONGTYPE", it.message ?: "Unknown error")
                    } else {
                        throw it
                    }
                }
            )
    }
}