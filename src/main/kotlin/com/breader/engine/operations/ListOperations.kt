package com.breader.engine.operations

interface ListOperations {
    fun lpush(key: String, values: List<String>): Int
    fun rpush(key: String, values: List<String>): Int
}