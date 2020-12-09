package ioc.registration.packagescanning

import ioc.registration.TypeRegistry
import kotlin.reflect.KClass

interface IBindingStrategy {
    fun bind(typeRegistry: TypeRegistry, classes : List<KClass<*>>)
}