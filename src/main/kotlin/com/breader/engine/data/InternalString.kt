package com.breader.engine.data

import java.time.Instant

data class InternalString(
    val value: String,
    override val expirationTime: Instant? = null
) : InternalData(expirationTime)