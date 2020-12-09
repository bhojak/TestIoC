package ioc.registration.conditionalbinding

import ioc.activation.ActivationContext

class AlwaysMatches : IBindingCondition {
    override fun matches(context : ActivationContext) : Boolean {
        return true
    }
}