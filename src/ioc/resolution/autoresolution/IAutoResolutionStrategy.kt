package ioc.resolution.autoresolution

import kotlin.reflect.KClass

interface IAutoResolutionStrategy{
    fun discover(requestedType: KClass<*>): KClass<*>?
}