package com.breader.engine

import com.breader.engine.data.InternalString
import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class StorageTest {

    @Test
    fun `should get value`() {
        // given
        val storage = initStorage {
            set("key", "value")
        }

        // when
        val result = storage.get("key")

        // then
        assertIs<InternalString>(result)
        assertEquals("value", result.value)
    }

    @Test
    fun `should not get value if it expired`() {
        // given
        val fixedClock = Clock.fixed(Instant.now(), ZoneId.systemDefault())
        val storage = initStorage(fixedClock) {
            setExpiring("key", "value", Instant.now(fixedClock).minusSeconds(5))
        }

        // when
        val result = storage.get("key")

        // then
        assertEquals(null, result)
    }

    @Test
    fun `should report value as existing`() {
        // given
        val storage = initStorage {
            set("key", "value")
        }

        // when + then
        assertTrue { storage.exists("key") }
    }

    @Test
    fun `should set value`() {
        // given
        val storage = Storage()

        // when
        storage.set("key", "value")

        // then
        assertEquals("value", (storage.get("key") as? InternalString)?.value)
    }

    @Test
    fun `should set value even when it already exists`() {
        // given
        val storage = initStorage {
            set("key", "value")
        }

        val modifiedValue = "modifiedValue"

        // when
        storage.set("key", modifiedValue)

        // then
        assertEquals(modifiedValue, storage.getString("key"))
    }

    @Test
    fun `should set value if absent`() {
        // given
        val storage = initStorage {
            set("key", "value")
        }

        // when
        storage.setIfAbsent("key", "value")

        // then
        assertEquals("value", storage.getString("key"))
    }

    @Test
    fun `should not set value if it already exists and set if absent is used`() {
        // given
        val storage = initStorage {
            set("key", "value")
        }

        val modifiedValue = "modifiedValue"

        // when
        storage.setIfAbsent("key", modifiedValue)

        // then
        assertEquals("value", storage.getString("key"))
    }

    @Test
    fun `should set value if it already exist but expired and set if absent is used`() {
        // given
        val fixedClock = Clock.fixed(Instant.now(), ZoneId.systemDefault())
        val storage = initStorage(fixedClock) {
            setExpiring("key", "value", Instant.now(fixedClock).minusSeconds(5))
        }

        // when
        storage.setIfAbsent("key", "modifiedValue")

        // then
        assertEquals("modifiedValue", storage.getString("key"))
    }
}

private fun Storage.getString(key: String): String? = (this.get(key) as? InternalString)?.value