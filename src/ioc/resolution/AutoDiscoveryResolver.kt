package ioc.resolution

import ioc.resolution.autoresolution.IAutoResolutionStrategy
import ioc.resolution.autoresolution.ResolveClassToClassImplementationStrategy
import ioc.resolution.autoresolution.ResolveConcreteTypeToSelfStrategy
import ioc.resolution.autoresolution.ResolveInterfaceToClassStrategy
import kotlin.reflect.KClass

class AutoDiscoveryResolver {
    var strategies: MutableList<IAutoResolutionStrategy> = mutableListOf(
            ResolveConcreteTypeToSelfStrategy(),
            ResolveInterfaceToClassStrategy(),
            ResolveClassToClassImplementationStrategy()
    )

    fun selectTypeFor(requestedType: KClass<*>): KClass<*> {
        for (stat in strategies) {
            val match = stat.discover(requestedType)
            if (match != null) {
                return match
            }
        }
        return requestedType
    }
}