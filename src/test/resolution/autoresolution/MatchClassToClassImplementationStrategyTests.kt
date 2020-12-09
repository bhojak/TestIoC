package test.resolution.autoresolution

import ioc.resolution.autoresolution.ResolveClassToClassImplementationStrategy
import test.data2.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertNull

class MatchClassToClassImplementationStrategyTests {

    private lateinit var _resolver: ResolveClassToClassImplementationStrategy

    @BeforeEach
    fun setUp() {
        _resolver = ResolveClassToClassImplementationStrategy()
    }

    @Test
    fun discover_passedTypeNoMatchingConcreteImplFound_ReturnsNull() {
        val target = _resolver.discover(BarBar::class)

        assertNull(target)
    }

    @Test
    fun discover_MatchingNameFoundButDoesntImplementInterface_ReturnsNull() {
        val target = _resolver.discover(InterfaceBlah2::class)

        assertNull(target)
    }
}