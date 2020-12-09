package ioc.registration

import ioc.registration.conditionalbinding.AlwaysMatches
import ioc.registration.conditionalbinding.IBindingCondition
import kotlin.reflect.KClass

class Binding {
    var sourceType: KClass<*>
    var targetType: KClass<*>? = null
    var lifecycle: Lifecycle = Lifecycle.PerRequest
    var targetDelegate: () -> Any? = { null }
    var condition: IBindingCondition = AlwaysMatches()

    constructor(
            sourceType: KClass<*>,
            targetType: KClass<*>,
            lifecycle: Lifecycle,
            condition: IBindingCondition? = null
    ) {
        this.sourceType = sourceType
        this.targetType = targetType
        this.lifecycle = lifecycle
        if(condition != null) this.condition = condition
    }

    constructor(
            sourceType: KClass<*>,
            targetDelegate: () -> Any,
            lifecycle: Lifecycle,
            condition: IBindingCondition? = null
    ) {
        this.sourceType = sourceType
        this.targetDelegate = targetDelegate
        this.lifecycle = lifecycle
        if(condition != null) this.condition = condition
    }
}