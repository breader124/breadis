package com.breader.command.impl

import com.breader.engine.Storage
import com.breader.engine.data.InternalString
import com.breader.protocol.type.BulkStringData
import com.breader.protocol.type.Data
import com.breader.protocol.type.ErrorData
import com.breader.protocol.type.NullData

class GetCommand(private val storage: Storage) : Command {

    override fun name() = BulkStringData("GET")

    override fun execute(args: List<BulkStringData>): Data {
        if (args.size != 2) {
            return ErrorData("ARG", "GET command requires 2 arguments")
        }

        val key = args[1].value
        return storage.get(key)?.let {
            if (it is InternalString) {
                BulkStringData(it.value)
            } else {
                ErrorData("WRONGTYPE", "Operation against a key holding the wrong kind of value")
            }
        } ?: NullData()
    }
}