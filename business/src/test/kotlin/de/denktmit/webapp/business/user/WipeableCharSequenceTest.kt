package de.denktmit.webapp.business.user

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.security.crypto.factory.PasswordEncoderFactories

class WipeableCharSequenceTest {

    private val stringPassword = "somePassword"
    private val charsPassword = "somePassword".toCharArray()
    private lateinit var wipeableCharSequence: WipeableCharSequence

    @BeforeEach
    fun setUp() {
        wipeableCharSequence = WipeableCharSequence(charsPassword)
    }

    @Test
    fun passwordEncoderSupport() {
        val encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()
        val stringHashedPassword = encoder.encode(stringPassword)
        assertThat(encoder.matches(wipeableCharSequence, stringHashedPassword)).isTrue
    }


    @Test
    fun getLength() {
        assertThat(wipeableCharSequence.length).isEqualTo(stringPassword.length)
    }

    @Test
    fun get() {
        val resultChars = CharArray(wipeableCharSequence.length)
        for ((index, char) in wipeableCharSequence.withIndex()) {
            resultChars[index] = char
        }
        val actualPassword = resultChars.concatToString()
        assertThat(actualPassword).isEqualTo(stringPassword)
    }

    @Test
    fun testToString() {
        assertThat(wipeableCharSequence.toString()).isEqualTo(stringPassword)
    }

    @Test
    fun subSequence() {
        val expectedSequence = WipeableCharSequence("mePa".toCharArray())
        assertThat(wipeableCharSequence.subSequence(2, 6)).isEqualTo(expectedSequence)
    }

    @Test
    fun wipe() {
        wipeableCharSequence.wipe()
        val wipedPassword = wipeableCharSequence.toString()
        val expectedWipedPassword = "\u0000".repeat(charsPassword.size)
        assertThat(wipedPassword).isEqualTo(expectedWipedPassword)
    }
}