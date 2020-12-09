package test.registration.packagescanning

import ioc.registration.Lifecycle
import ioc.registration.TypeRegistry
import ioc.registration.conditionalbinding.WhenInjectedInto
import ioc.registration.packagescanning.AutoDiscovery
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import test.data2.*
import kotlin.test.*

class AutoDiscoveryTests {

    private lateinit var _registry: TypeRegistry
    private lateinit var _discovery: AutoDiscovery

    @BeforeEach
    fun setUp() {
        _registry = TypeRegistry()
        _registry.autoDiscovery = false
        _discovery = AutoDiscovery(_registry)
    }
}