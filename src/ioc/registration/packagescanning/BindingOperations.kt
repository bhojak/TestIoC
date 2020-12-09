package ioc.registration.packagescanning

import ioc.registration.Lifecycle
import ioc.registration.conditionalbinding.BindingConditions
import ioc.registration.conditionalbinding.IBindingCondition

class BindingOperations {
    @JvmOverloads
    fun bindAllInterfaces(
            condition: ((op: BindingConditions) -> IBindingCondition)? = null,
            lifecycle: Lifecycle? = null
    ): IBindingStrategy {
        return BindAllInterfaces(condition, lifecycle)
    }

    @JvmOverloads
    fun bindClassesToSelf(
            condition: ((op: BindingConditions) -> IBindingCondition)? = null,
            lifecycle: Lifecycle? = null
    ): IBindingStrategy {
        return BindClassesToSelf(condition, lifecycle)
    }

    @JvmOverloads
    fun bindClassesAndInterfaces(
            condition: ((op: BindingConditions) -> IBindingCondition)? = null,
            lifecycle: Lifecycle? = null
    ): IBindingStrategy {
        return BindClassesAndInterfaces(condition, lifecycle)
    }
}