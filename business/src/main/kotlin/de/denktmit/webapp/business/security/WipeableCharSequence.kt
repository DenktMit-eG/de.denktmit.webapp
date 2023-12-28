package de.denktmit.webapp.business.security

import java.util.*

class WipeableCharSequence(private val data: CharArray): CharSequence, MemoryWipeable {

    override val length: Int
        get() = data.size

    override fun get(index: Int): Char {
        return data[index]
    }

    override fun subSequence(startIndex: Int, endIndex: Int): CharSequence {
        return String(data, startIndex, endIndex - startIndex)
    }

    override fun wipe() {
        Arrays.fill(data, '\u0000')
    }

}


