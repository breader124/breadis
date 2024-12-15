package com.breader.engine

import com.breader.engine.type.NativeType
import java.util.concurrent.ConcurrentHashMap

class Storage {
    private val storage = ConcurrentHashMap<String, NativeType>()

    /*
    Things to consider while designing the storage:
    - there is a number of options related to setting the data
        - only set if the value does not exist
        - only set if the value exists
    - there are numerous expiry options supported in Redis
    - the storage should be able to handle different types of data
        - there are commands operating on simple strings
        - but there are also commands operating on e.g. lists
    - how to make concurrent access safe for operations depending on the current state
        - adding value to the list associated with the key
        - incrementing the integer
    - how to achieve tha above without leaking the fact that ConcurrentHashMap is used on the storage level

     Things that I don't know how to treat yet:
     - GET can be chained with SET (can other commands be chained?)
     */
}