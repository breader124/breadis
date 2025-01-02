package com.breader.engine.operations

import java.time.Instant

interface StringOperations {
    fun set(key: String, value: String)
    fun setIfAbsent(key: String, value: String)
    fun setExpiring(key: String, value: String, expirationTime: Instant)
    fun setIfAbsentExpiring(key: String, value: String, expirationTime: Instant)
}