package com.breader.command

import com.breader.command.impl.Command
import com.breader.command.impl.EchoCommand
import com.breader.command.impl.PingCommand
import com.breader.protocol.type.ArrayData
import com.breader.protocol.type.BulkStringData
import com.breader.protocol.type.Data

class CommandDispatcher {
    private val commands: List<Command> = listOf(PingCommand(), EchoCommand())
    private val commandAssociation = mutableMapOf<BulkStringData, Command>()

    init {
        commands.forEach { commandAssociation.put(it.name(), it) }
    }

    fun dispatch(command: Data): Data {
        require(command is ArrayData) { "Command must be an array" }

        val commandArguments = command.value.filterIsInstance<BulkStringData>()
        require(command.value.size == commandArguments.size) { "All command args must be of BulkStringData type" }

        val chosenCommand = commandAssociation[commandArguments[0]] ?: throw IllegalArgumentException("Unknown command")
        return chosenCommand.execute(commandArguments)

    }
}