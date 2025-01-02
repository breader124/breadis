package com.breader.engine.operations

import com.breader.engine.data.InternalData

interface BasicOperations {
    fun get(key: String): InternalData?
    fun exists(key: String): Boolean
    fun delete(key: String): Boolean
}