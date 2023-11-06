package de.denktmit.webapp.persistence

import java.time.Instant

object Constants {

    /**
     * This constant represents something in the far past. It is usually
     * used to express some eternal duration into the past without falling
     * back to nullable types
     */
    val FAR_PAST = Instant.parse("1000-01-01T00:00:00Z")

    /**
     * This constant represents something in the far future. It is usually
     * used to express some eternal duration into the future without falling
     * back to nullable types
     */
    val FAR_FUTURE = Instant.parse("3000-01-01T00:00:00Z")
}