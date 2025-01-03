package com.breader.engine

import com.breader.engine.data.InternalData
import com.breader.engine.data.InternalList
import com.breader.engine.data.InternalString
import com.breader.engine.operations.BasicOperations
import com.breader.engine.operations.ListOperations
import com.breader.engine.operations.StringOperations
import java.time.Clock
import java.time.Instant
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class Storage(
    private val clock: Clock = Clock.systemDefaultZone(),
    private val internalStorage: MutableMap<StorageKey, InternalData> = ConcurrentHashMap<StorageKey, InternalData>()
) : BasicOperations, StringOperations, ListOperations {

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

    // necessary due to the missing active expiration mechanism, this behaviour fakes its existence to the client
    override fun delete(key: String): Boolean {
        val storageKey = StorageKey(key)
        return internalStorage.remove(storageKey)?.let {
            it.expirationTime?.isAfter(Instant.now(clock)) != false
        } == true
    }

    override fun set(key: String, value: String) {
        val storageKey = StorageKey(key)
        val value = InternalString(value)
        internalStorage.put(storageKey, value)
    }

    override fun setIfAbsent(key: String, value: String) {
        val storageKey = StorageKey(key)
        val newValue = InternalString(value)
        internalStorage.compute(storageKey) { _, oldValue ->
            if (oldValue == null || oldValue.expirationTime?.isBefore(Instant.now(clock)) == true) {
                newValue
            } else {
                oldValue
            }
        }
    }

    override fun setExpiring(key: String, value: String, expirationTime: Instant) {
        val storageKey = StorageKey(key)
        val value = InternalString(value, expirationTime)
        internalStorage.put(storageKey, value)
    }

    override fun setIfAbsentExpiring(key: String, value: String, expirationTime: Instant) {
        val storageKey = StorageKey(key)
        val newValue = InternalString(value, expirationTime)
        internalStorage.compute(storageKey) { _, oldValue ->
            if (oldValue == null || oldValue.expirationTime?.isAfter(Instant.now(clock)) == true) {
                newValue
            } else {
                oldValue
            }
        }
    }

    override fun lpush(key: String, values: List<String>): Int =
        push(key, values) { oldList, newElem -> oldList.addFirst(newElem) }

    override fun rpush(key: String, values: List<String>): Int =
        push(key, values) { oldList, newElem -> oldList.addLast(newElem) }

    private fun push(key: String, values: List<String>, mergeFun: (LinkedList<InternalString>, InternalString) -> Unit): Int {
        val storageKey = StorageKey(key)
        val newElems = LinkedList<InternalString>()
            .also { values.forEach { value -> it.add(InternalString(value)) } }
            .let { InternalList(it) }

        var modifiedListLen = values.size
        internalStorage.merge(storageKey, newElems) { oldList, newElems ->
            if (oldList !is InternalList || newElems !is InternalList) {
                throw IllegalArgumentException("Key $key is not a list")
            }
            modifiedListLen += oldList.value.size

            oldList.also { newElems.value.forEach { v -> mergeFun(it.value, v) } }
        }

        return modifiedListLen
    }
}

fun initStorage(init: Storage.() -> Unit): Storage = Storage().apply(init)

fun initStorage(clock: Clock, init: Storage.() -> Unit): Storage = Storage(clock).apply(init)