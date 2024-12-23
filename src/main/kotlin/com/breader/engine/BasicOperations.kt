package com.breader.engine

import com.breader.engine.data.InternalData

interface BasicOperations {
    fun get(key: String): InternalData?
    fun set(key: String, value: InternalData): InternalData?
    fun setIfAbsent(key: String, value: InternalData): InternalData?
}