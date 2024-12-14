package com.breader.command.impl

import com.breader.protocol.type.BulkStringData
import com.breader.protocol.type.Data
import com.breader.protocol.type.ErrorData
import com.breader.protocol.type.SimpleStringData

class PingCommand : Command {
    override fun name() = BulkStringData("PING")

    override fun execute(args: List<BulkStringData>): Data {
        if (args.size > 2) {
            return ErrorData("ARG", "Invalid number of arguments for PING command")
        }

        return if (args.size == 1) {
            SimpleStringData("PONG")
        } else {
            args[1]
        }
    }
}