package com.breader.engine.data

import java.time.Instant
import java.util.LinkedList

data class InternalList(
    val value: LinkedList<InternalString>,
    override val expirationTime: Instant? = null
) : InternalData(expirationTime)