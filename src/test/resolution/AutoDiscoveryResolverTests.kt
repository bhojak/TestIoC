package test.resolution

import ioc.resolution.AutoDiscoveryResolver
import ioc.resolution.autoresolution.IAutoResolutionStrategy
import org.junit.jupiter.api.Test
import test.data2.InterfaceFoo
import kotlin.reflect.KClass
import kotlin.test.assertTrue

class AutoDiscoveryResolverTests {
    @Test
    fun selectTypeFor_PassedInterface_DelegatesToExternalStrategies() {
        val resolver = AutoDiscoveryResolver()
        val spy = DiscoveryStrategySpy()
        resolver.strategies.clear()
        resolver.strategies.add(spy)

        resolver.selectTypeFor(InterfaceFoo::class)

        assertTrue(spy.wasCalled)
    }

    class DiscoveryStrategySpy : IAutoResolutionStrategy {
        var wasCalled = false
        override fun discover(requestedType: KClass<*>): KClass<*>? {
            wasCalled = true
            return null
        }
    }
}