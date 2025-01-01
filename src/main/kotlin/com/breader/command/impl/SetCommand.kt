package com.breader.command.impl

import com.breader.engine.Storage
import com.breader.protocol.type.BulkStringData
import com.breader.protocol.type.Data
import com.breader.protocol.type.ErrorData
import com.breader.protocol.type.SimpleStringData
import java.time.Clock
import java.time.Instant

class SetCommand(
    private val storage: Storage,
    private val clock: Clock = Clock.systemDefaultZone()
) : Command {

    override fun name() = BulkStringData("SET")

    override fun execute(args: List<BulkStringData>): Data {
        if (args.size == 3) {
            storage.set(args[1].value, args[2].value)
            return SimpleStringData("OK")
        }

        if (args.size == 5) {
            val expirationOption = args[3].value
            val expirationAbsoluteValue = args[4].value.toLong()

            val expirationTime = when (expirationOption) {
                "EX" -> clock.instant().plusSeconds(expirationAbsoluteValue)
                "PX" -> clock.instant().plusMillis(expirationAbsoluteValue)
                "EXAT" -> Instant.ofEpochSecond(expirationAbsoluteValue)
                "PXAT" -> Instant.ofEpochMilli(expirationAbsoluteValue)
                else -> return ErrorData("ARG", "Allowed expiration options are EX, PX, EXAT, PXAT")
            }
            storage.setExpiring(args[1].value, args[2].value, expirationTime)

            return SimpleStringData("OK")
        }

        return ErrorData("ARG", "Wrong number of arguments for 'SET' command")
    }
}