package test.resolution.autoresolution

import ioc.resolution.autoresolution.ResolveConcreteTypeToSelfStrategy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import test.data2.Foo
import test.data2.InterfaceFoo
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ResolveConcreteTypeToSelfStrategyTests {
    private lateinit var _resolver: ResolveConcreteTypeToSelfStrategy

    @BeforeEach
    fun setUp() {
        _resolver = ResolveConcreteTypeToSelfStrategy()
    }

    @Test
    fun discover_concreteClass_returnsSelf() {
        val target = _resolver.discover(Foo::class)!!

        assertEquals(Foo::class, target)
    }

    @Test
    fun discover_interfaceSupplied_returnsNull() {
        val target = _resolver.discover(InterfaceFoo::class)

        assertNull(target)
    }
}