package com.breader.engine

import com.breader.engine.data.InternalData
import com.breader.engine.data.InternalString
import java.time.Clock
import java.time.Instant
import java.util.concurrent.ConcurrentHashMap

class Storage(
    private val clock: Clock = Clock.systemDefaultZone(),
    private val internalStorage: MutableMap<StorageKey, InternalData> = ConcurrentHashMap<StorageKey, InternalData>()
) : BasicOperations {

    override fun get(key: String): InternalData? {
        val storageKey = StorageKey(key)
        return internalStorage.compute(storageKey) { _, oldValue ->
            if (oldValue?.expirationTime?.isBefore(clock.instant()) == true) {
                null
            } else {
                oldValue
            }
        }
    }

    override fun exists(key: String): Boolean = get(key) != null

    override fun set(key: String, value: String): InternalData? {
        val storageKey = StorageKey(key)
        val value = InternalString(value)
        return internalStorage.put(storageKey, value)
    }

    override fun setExpiring(key: String, value: String, expirationTime: Instant): InternalData? {
        val storageKey = StorageKey(key)
        val value = InternalString(value, expirationTime)
        return internalStorage.put(storageKey, value)
    }

    override fun setIfAbsent(key: String, value: String): InternalData? {
        val storageKey = StorageKey(key)
        val newValue = InternalString(value)
        return internalStorage.compute(storageKey) { _, oldValue ->
            if (oldValue == null || oldValue.expirationTime?.isBefore(Instant.now(clock)) == true) {
                newValue
            } else {
                oldValue
            }
        }
    }

    override fun setIfAbsentExpiring(key: String, value: String, expirationTime: Instant): InternalData? {
        val storageKey = StorageKey(key)
        val newValue = InternalString(value, expirationTime)
        return internalStorage.compute(storageKey) { _, oldValue ->
            if (oldValue == null || oldValue.expirationTime?.isAfter(Instant.now(clock)) == true) {
                newValue
            } else {
                oldValue
            }
        }
    }

    // necessary due to the missing active expiration mechanism, this behaviour fakes its existence to the client
    override fun delete(key: String): Boolean {
        val storageKey = StorageKey(key)
        return internalStorage.remove(storageKey)?.let {
            it.expirationTime?.isAfter(Instant.now(clock)) != false
        } == true
    }
}

fun initStorage(init: Storage.() -> Unit): Storage = Storage().apply(init)

fun initStorage(clock: Clock, init: Storage.() -> Unit): Storage = Storage(clock).apply(init)