package ioc.registration.packagescanning

import ioc.registration.Lifecycle
import ioc.registration.conditionalbinding.BindingConditions
import ioc.registration.conditionalbinding.IBindingCondition

abstract class BindingStrategyBase(
        val condition: ((op: BindingConditions) -> IBindingCondition)? = null,
        lifecycle: Lifecycle? = null
) : IBindingStrategy {
    val lifeCycle : Lifecycle? = lifecycle
}