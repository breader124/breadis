package com.breader.command.impl

import com.breader.protocol.type.BulkStringData
import com.breader.protocol.type.Data

interface Command {
    fun name(): BulkStringData
    fun execute(args: List<BulkStringData>): Data
}