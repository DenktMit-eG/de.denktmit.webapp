package de.denktmit.webapp.persistence

import java.time.Instant

object Constants {

    /**
     * This constant represents something in the far future. It is usually
     * used to express some eternal duration without falling back to
     * nullable types
     */
    val FAR_FUTURE = Instant.parse("9999-12-31T23:59:59.999999Z")
}