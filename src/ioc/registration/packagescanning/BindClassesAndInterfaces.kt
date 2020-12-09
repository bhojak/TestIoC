package ioc.registration.packagescanning

import ioc.registration.Lifecycle
import ioc.registration.TypeRegistry
import ioc.registration.conditionalbinding.BindingConditions
import ioc.registration.conditionalbinding.IBindingCondition
import kotlin.reflect.KClass

class BindClassesAndInterfaces(condition: ((op: BindingConditions) -> IBindingCondition)?, lifecycle: Lifecycle?) :
        BindingStrategyBase(condition, lifecycle) {
    private var _classes: BindClassesToSelf = BindClassesToSelf(condition, lifecycle)
    private var _interfaces: BindAllInterfaces = BindAllInterfaces(condition, lifecycle)

    override fun bind(typeRegistry: TypeRegistry, classes: List<KClass<*>>) {
        _classes.bind(typeRegistry, classes)
        _interfaces.bind(typeRegistry, classes)
    }
}