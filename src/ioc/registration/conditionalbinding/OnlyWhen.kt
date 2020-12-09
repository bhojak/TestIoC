package ioc.registration.conditionalbinding

import ioc.activation.ActivationContext

class OnlyWhen(val filter: ((ctx: ActivationContext) -> Boolean)) : IBindingCondition {

    override fun matches(context : ActivationContext): Boolean {
        return filter(context)
    }
}