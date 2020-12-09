package ioc

import ioc.activation.ActivationContext
import ioc.activation.IActivateTypes
import ioc.activation.LifeCycleManagingTypeActivator
import ioc.activation.TypeActivator
import ioc.registration.TypeRegistry
import kotlin.reflect.KClass

class IoCContainer {

    @get:JvmName("registrations")
    val registrations : TypeRegistry
    private val creator : IActivateTypes

    constructor () : this(TypeRegistry())
    constructor (typeRegistry : TypeRegistry) {
        registrations = typeRegistry
        creator = LifeCycleManagingTypeActivator(TypeActivator(registrations))
    }

    inline fun <reified T: Any> resolve(): T {
        return resolve(T::class) as T
    }

    fun resolve(requestedType: KClass<*>): Any {
        val bindings = registrations.retrieveBindingFor(requestedType)
        val activationContext = ActivationContext(requestedType)

        try {
            return creator.create(bindings, activationContext)
        } catch (ex : StackOverflowError){
            throw CircularDependencyException(ex)
        }
    }

    // For Java
    fun resolve(requestedType: Class<*>) : Any {
        return resolve(requestedType.kotlin)
    }
    // For Java
}