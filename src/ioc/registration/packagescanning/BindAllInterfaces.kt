package ioc.registration.packagescanning

import ioc.registration.Lifecycle
import ioc.registration.TypeRegistry
import ioc.registration.conditionalbinding.BindingConditions
import ioc.registration.conditionalbinding.IBindingCondition
import kotlin.reflect.KClass

class BindAllInterfaces(condition: ((op: BindingConditions) -> IBindingCondition)?, lifecycle: Lifecycle?) :
        BindingStrategyBase(condition, lifecycle) {
    override fun bind(typeRegistry: TypeRegistry, classes: List<KClass<*>>) {
        classes
                .filter { x -> !x.java.isInterface }
                .forEach { c -> c.java.interfaces.forEach { i -> typeRegistry.bind(i.kotlin, c, condition, lifeCycle) }}
    }
}