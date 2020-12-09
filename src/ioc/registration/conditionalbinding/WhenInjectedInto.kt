package ioc.registration.conditionalbinding

import ioc.activation.ActivationContext
import kotlin.reflect.KClass

class WhenInjectedInto(val target: KClass<*>) : IBindingCondition {

    override fun matches(context : ActivationContext): Boolean {
        val injectingInto = context.activationHistory.last().first

        if(injectingInto == target){
            return true
        }

        return false
    }
}