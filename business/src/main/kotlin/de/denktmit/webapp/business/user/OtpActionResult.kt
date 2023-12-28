package de.denktmit.webapp.business.user

sealed class OtpActionResult<T: Any, E: Any> {
    data class Success<T: Any, E: Any>(val payload: T): OtpActionResult<T, E>()
    data class Failed<T: Any, E: Any>(val errorPayload: E): OtpActionResult<T, E>()
}