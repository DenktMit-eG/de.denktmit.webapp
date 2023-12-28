package de.denktmit.webapp.persistence.users

import de.denktmit.webapp.persistence.BusinessKey

data class UserKey(
    val mail: String
): BusinessKey<User>