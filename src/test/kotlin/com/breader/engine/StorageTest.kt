package com.breader.engine

import com.breader.engine.data.InternalString
import kotlin.test.Test
import kotlin.test.assertEquals

class StorageTest {

    @Test
    fun `should set value`() {
        // given
        val storage = Storage()

        val key = "key"
        val value = InternalString("value")

        // when
        storage.set(key, value)

        // then
        assertEquals(value, storage.get(key))
    }

    @Test
    fun `should set value even when it already exists`() {
        // given
        val storage = Storage()

        val key = "key"
        val value = InternalString("value")
        storage.set(key, value)

        val modifiedValue = InternalString("modifiedValue")

        // when
        storage.set(key, modifiedValue)

        // then
        assertEquals(modifiedValue, storage.get(key))
    }

    @Test
    fun `should set value if absent`() {
        // given
        val storage = Storage()

        val key = "key"
        val value = InternalString("value")

        // when
        storage.setIfAbsent(key, value)

        // then
        assertEquals(value, storage.get(key))
    }

    @Test
    fun `should not set value if it already exists and set if absent is used`() {
        // given
        val storage = Storage()
        
        val key = "key"
        val value = InternalString("value")
        storage.set(key, value)

        val modifiedValue = InternalString("modifiedValue")

        // when
        storage.setIfAbsent(key, modifiedValue)

        // then
        assertEquals(value, storage.get(key))
    }
}