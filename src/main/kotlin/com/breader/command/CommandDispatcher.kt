package com.breader.command

import com.breader.command.impl.*
import com.breader.engine.Storage
import com.breader.protocol.type.ArrayData
import com.breader.protocol.type.BulkStringData
import com.breader.protocol.type.Data

class CommandDispatcher(storage: Storage) {

    private val commands: List<Command> = listOf(
        PingCommand(),
        EchoCommand(),
        GetCommand(storage),
        ExistsCommand(storage),
        SetCommand(storage),
        DelCommand(storage)
    )
    private val commandAssociation = mutableMapOf<BulkStringData, Command>()

    init {
        commands.forEach { commandAssociation.put(it.name(), it) }
    }

    fun dispatch(command: Data): Data {
        require(command is ArrayData) { "Command must be an array" }

        val commandArguments = command.value.filterIsInstance<BulkStringData>()
        require(command.value.size == commandArguments.size) { "All command args must be of BulkStringData type" }

        val chosenCommand = commandAssociation.retrieveCommandCaseInsensitive(commandArguments[0])
        return chosenCommand.execute(commandArguments)

    }

    private fun Map<BulkStringData, Command>.retrieveCommandCaseInsensitive(commandName: BulkStringData): Command =
        this[commandName]
            ?: this[BulkStringData(commandName.value.uppercase())]
            ?: throw IllegalArgumentException("Unknown command")
}