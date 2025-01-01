package com.breader.command.impl

import com.breader.protocol.type.BulkStringData
import com.breader.protocol.type.Data
import com.breader.protocol.type.ErrorData

class EchoCommand : Command {

    override fun name() = BulkStringData("ECHO")

    override fun execute(args: List<BulkStringData>): Data {
        if (args.size != 2) {
            return ErrorData("ARG", "Invalid number of arguments for ECHO command")
        }

        return args[1]
    }
}