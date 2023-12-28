package de.denktmit.webapp.business.security

interface MemoryWipeable {

    /**
     * Removes data from memory without waiting for garbage collection, e.g. nulling
     * a character array or pointers
     */
    fun wipe()
}
