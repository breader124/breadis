package com.breader.command.impl

import com.breader.engine.Storage
import com.breader.protocol.type.BulkStringData
import com.breader.protocol.type.Data
import com.breader.protocol.type.ErrorData
import com.breader.protocol.type.SimpleStringData

class SetCommand(private val storage: Storage) : Command {

    override fun name() = BulkStringData("SET")

    override fun execute(args: List<BulkStringData>): Data {
        if (args.size != 3) {
            return ErrorData("ARG", "SET command requires 3 arguments")
        }

        storage.set(args[1].value, args[2].value)
        return SimpleStringData("OK")
    }
}