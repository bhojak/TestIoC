package ioc.activation

import kotlin.reflect.KClass

class ActivationContext(val rootType: KClass<*>) {
    val activationHistory : MutableList<Pair<KClass<*>, KClass<*>>> = mutableListOf(Pair(rootType, rootType))
}