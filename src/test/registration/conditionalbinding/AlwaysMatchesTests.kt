package test.registration.conditionalbinding

import ioc.activation.ActivationContext
import ioc.registration.conditionalbinding.AlwaysMatches
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import test.data2.Bar
import kotlin.test.assertTrue

class AlwaysMatchesTests {
    private lateinit var _condition: AlwaysMatches

    @BeforeEach
    fun setUp() {
        _condition = AlwaysMatches()
    }

    @Test
    fun match_ReturnsTrue() {
        val context = ActivationContext(Bar::class)

        val result = _condition.matches(context)

        assertTrue(result)
    }
}