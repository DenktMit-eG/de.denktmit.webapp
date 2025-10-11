package de.denktmit.webapp.webwicket.utils

class CauseIterator(val throwable: Throwable) : Iterator<Throwable> {
    var current: Throwable = Throwable(throwable)

    override fun hasNext() = current.cause != null
    override fun next() = (
            current.cause
                ?: throw NoSuchElementException("Causes for $throwable are exhausted")
            )
        .also { current = it }
}

fun Throwable.causeIterator() = CauseIterator(this)

fun Throwable.exceptionMessages(withType: Boolean = true, separator: String = "\n  Caused by: ") =
    causeIterator().asSequence().map {
        (if (withType) "${it.javaClass.simpleName}: " else "") + it.message
    }.joinToString(separator)