package de.denktmit.webapp

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class HelloWorldKtTest {

    @Test
    fun sayHelloWorld() {
        assertThat(sayHello()).isEqualTo("Hello World")
    }

    @Test
    fun sayHelloDeveloper() {
        assertThat(sayHello("Developer")).isEqualTo("Hello Developer")
    }
}