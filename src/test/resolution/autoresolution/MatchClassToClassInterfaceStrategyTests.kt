package test.resolution.autoresolution

import ioc.resolution.autoresolution.ResolveInterfaceToClassStrategy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import test.data2.Foo
import test.data2.IBadImplementationCheck
import test.data2.INoneNameMatchingPair
import test.data2.InterfaceFoo
import kotlin.test.assertEquals
import kotlin.test.assertNull

class MatchClassToInterfaceStrategyTests {

    private lateinit var _resolver: ResolveInterfaceToClassStrategy

    @BeforeEach
    fun setUp() {
        _resolver = ResolveInterfaceToClassStrategy()
    }

    @Test
    fun discover_passedIPrefixedTypeNoMatchingConcreteFound_ReturnsNull() {
        val target = _resolver.discover(INoneNameMatchingPair::class)

        assertNull(target)
    }

    @Test
    fun discover_MatchingNameFoundButDoesntImplementInterface_ReturnsNull() {
        val target = _resolver.discover(IBadImplementationCheck::class)

        assertNull(target)
    }
}

