package test.registration.conditionalbinding

import ioc.activation.ActivationContext
import ioc.registration.conditionalbinding.WhenInjectedInto
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import test.data2.Foo
import test.data2.TypeWithDependency
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class WhenInjectedIntoTests {
    private lateinit var _condition: WhenInjectedInto

    @BeforeEach
    fun setUp() {
        _condition = WhenInjectedInto(TypeWithDependency::class)
    }

    @Test
    fun match_typeMatchesLastPartOfInjectionHistory_returnsTrue() {
        val context = ActivationContext(TypeWithDependency::class)

        val result = _condition.matches(context)

        assertTrue(result)
    }

    @Test
    fun match_typeDoesntMatchLastPartOfInjectionHistory_returnsFalse() {
        val context = ActivationContext(Foo::class)

        val result = _condition.matches(context)

        assertFalse(result)
    }
}

