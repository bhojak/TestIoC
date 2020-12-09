package ioc.activation

import ioc.registration.Binding


interface IActivateTypes {
    fun create(
            bindings: List<Binding>,
            activationContext: ActivationContext
    ): Any
}