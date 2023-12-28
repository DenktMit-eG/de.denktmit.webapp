package de.denktmit.webapp.business.user

import java.util.*

class WipeableCharSequence(private val data: CharArray): CharSequence, MemoryWipeable {

    override val length: Int
        get() = data.size

    override fun get(index: Int): Char {
        return data[index]
    }

    override fun subSequence(startIndex: Int, endIndex: Int): CharSequence {
        return WipeableCharSequence(data.slice(startIndex until endIndex).toCharArray())
    }

    override fun wipe() {
        Arrays.fill(data, '\u0000')
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is WipeableCharSequence) return false

        return data.contentEquals(other.data)
    }

    override fun hashCode(): Int {
        return data.contentHashCode()
    }

    override fun toString(): String {
        return data.concatToString()
    }


}


