package ioc.registration.packagescanning

import ioc.registration.Lifecycle
import ioc.registration.TypeRegistry
import ioc.registration.conditionalbinding.BindingConditions
import ioc.registration.conditionalbinding.IBindingCondition
import kotlin.reflect.KClass

class BindClassesToSelf(condition: ((op: BindingConditions) -> IBindingCondition)?, lifecycle: Lifecycle?) :
        BindingStrategyBase(condition, lifecycle) {
    override fun bind(typeRegistry: TypeRegistry, classes: List<KClass<*>>) {
        classes
                .filter { c -> !c.java.isInterface }
                .forEach { c -> typeRegistry.bind(c, c, condition, lifeCycle) }
    }
}