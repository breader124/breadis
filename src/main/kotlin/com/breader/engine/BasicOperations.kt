package com.breader.engine

import com.breader.engine.data.InternalData
import java.time.Instant

interface BasicOperations {
    fun get(key: String): InternalData?
    fun exists(key: String): Boolean

    fun set(key: String, value: String): InternalData?
    fun setExpiring(key: String, value: String, expirationTime: Instant): InternalData?

    fun setIfAbsent(key: String, value: String): InternalData?
    fun setIfAbsentExpiring(key: String, value: String, expirationTime: Instant): InternalData?

    fun delete(key: String): Boolean
}