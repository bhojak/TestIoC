package ioc.registration.conditionalbinding

import ioc.activation.ActivationContext

interface IBindingCondition {
    fun matches(context : ActivationContext) : Boolean
}