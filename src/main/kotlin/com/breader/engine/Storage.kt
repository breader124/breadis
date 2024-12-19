package com.breader.engine

class Storage : BasicOperations, IntegerOperations {
    /*
    Things to consider while designing the storage:
    - there is a number of options related to setting the data
        - only set if the value does not exist
        - only set if the value exists

        pass this info through set operation args and handle on the engine level

    - there are numerous expiry options supported in Redis

        pass this info through the set operations args and handle on the engine level (in all relevant operations)
        store the expiry info associated with the key

    - the storage should be able to handle different types of data
        - there are commands operating on simple strings
        - but there are also commands operating on e.g. lists

        InternalData has been introduced, it'll be implemented by all data types that can be stored,
        e.g. InternalString, InternalList etc

    - how to make concurrent access safe for operations depending on the current state
        - adding value to the list associated with the key
        - incrementing the integer

        operation will be implemented on the storage level, and it'll be a concern of the storage to handle the
        concurrency, commands will just delegate the operation forward

    - how to achieve tha above without leaking the fact that ConcurrentHashMap is used on the storage level

        through the properly designed interface
     */
}