package com.breader.engine

import com.breader.engine.data.InternalList
import com.breader.engine.data.InternalString
import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import kotlin.test.*

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

    @Test
    fun `should delete existing value`() {
        // given
        val storage = initStorage {
            set("key", "value")
        }

        // when + then
        assertTrue { storage.delete("key") }
    }

    @Test
    fun `should not delete non-existing value`() {
        // given
        val storage = Storage()

        // when + then
        assertFalse { storage.delete("key") }
    }

    @Test
    fun `should not delete expired value`() {
        // given
        val fixedClock = Clock.fixed(Instant.now(), ZoneId.systemDefault())
        val storage = initStorage(fixedClock) {
            setExpiring("key", "value", fixedClock.instant().minusSeconds(5))
        }

        // when
        assertFalse { storage.delete("key") }
    }

    @Test
    fun `should create a new list and push value into it when key does not exist`() {
        // given
        val storage = Storage()

        // when
        val result = storage.lpush("key", listOf("value"))

        // then
        assertEquals(1, result)
        assertEquals(listOf(InternalString("value")), storage.getList("key"))    }

    @Test
    fun `should push value into existing list`() {
        // given
        val storage = initStorage {
            lpush("key", listOf("value1"))
        }

        // when
        val result = storage.lpush("key", listOf("value2"))

        // then
        assertEquals(2, result)
        assertEquals(
            listOf(
                InternalString("value2"),
                InternalString("value1")
            ),
            storage.getList("key")
        )
    }

    @Test
    fun `should push multiple values into existing list`() {
        // given
        val storage = initStorage {
            lpush("key", listOf("value1"))
        }

        // when
        val result = storage.lpush("key", listOf("value2", "value3"))

        // then
        assertEquals(3, result)
        assertEquals(
            listOf(
                InternalString("value3"),
                InternalString("value2"),
                InternalString("value1")
            ),
            storage.getList("key")
        )
    }

    @Test
    fun `should not push value into non-list key`() {
        // given
        val storage = initStorage {
            set("key", "value")
        }

        // when + then
        assertFailsWith<IllegalArgumentException> { storage.lpush("key", listOf("value")) }
    }
}

private fun Storage.getString(key: String): String? = (this.get(key) as? InternalString)?.value

private fun Storage.getList(key: String): List<InternalString>? = (this.get(key) as? InternalList)?.value