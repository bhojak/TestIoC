package test.registration

import ioc.activation.MissingBindingException
import ioc.registration.TypeRegistry
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import test.data2.*
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class TypeRegistryTests{

    private lateinit var _registry: TypeRegistry

    @BeforeEach
    fun setUp(){
        _registry = TypeRegistry()
    }

    @Test
    fun bindT_AllowsTypesToBeCreated() {
        _registry.autoDiscovery = false

        _registry.bind<InterfaceFoo, Foo>()

        val instance = _registry.retrieveBindingFor(InterfaceFoo::class).single()
        assertNotNull(instance.targetType)
    }

    @Test
    fun bind_NoAutoDiscovery_AllowsTypesToBeCreated() {
        _registry.autoDiscovery = false

        _registry.bind(Foo::class, Foo::class)

        val instance = _registry.retrieveBindingFor(Foo::class).single()
        assertNotNull(instance.targetType)
    }

    @Test
    fun bindSelfT_AllowsTypesToBeCreated() {
        _registry.autoDiscovery = false

        _registry.bindSelf<Foo>()

        val instance = _registry.retrieveBindingFor(Foo::class).single()
        assertNotNull(instance.targetType)
    }

    @Test
    fun bind_ToSelf_AllowsTypesToBeCreated() {
        _registry.autoDiscovery = false

        _registry.bind(Foo::class)

        val instance = _registry.retrieveBindingFor(Foo::class).single()
        assertNotNull(instance.targetType)
    }

    @Test
    fun bindTToFactoryFunction_AllowsTypesToBeCreated() {
        _registry.autoDiscovery = false

        _registry.bind<InterfaceFoo>({ Foo() })

        val instance = _registry.retrieveBindingFor(InterfaceFoo::class).single()
        assertNotNull(instance.targetDelegate)
    }

    @Test
    fun bind_ToFactoryFunction_AllowsTypesToBeCreated() {
        _registry.autoDiscovery = false

        _registry.bind(Foo::class, { Foo() })

        val instance = _registry.retrieveBindingFor(Foo::class).single()
        assertNotNull(instance.targetDelegate)
    }

    @Test
    fun retrieveBindingFor_NoBindingsNoDiscovery_Throws() {
        _registry.autoDiscovery = false

        assertThrows<MissingBindingException> {
            _registry.retrieveBindingFor(Foo::class)
        }
    }

    @Test
    fun retrieveBindingFor_NoBindings_CanResolveTypeWithNoDependencies() {
        val instance = _registry.retrieveBindingFor(Foo::class).single()

        assertNotNull(instance.targetType)
    }

    @Test
    fun retrieveBindingFor_NoBindings_CanResolveTypeWithDependencies(){
        val instance = _registry.retrieveBindingFor(TypeWithDependency::class).single()

        assertNotNull(instance.targetType)
    }

    @Test
    fun retrieveBindingFor_NoBindings_CanResolveInterfaceWithDefaultImplementation(){
        val instance = _registry.retrieveBindingFor(InterfaceFoo::class).single()

        assertNotNull(instance.targetType)
    }

    @Test
    fun retrieveBindingFor_bindingPresent_CanResolveToBoundImplementation(){
        _registry.bind(InterfaceFoo::class, Foo2::class)

        val instance = _registry.retrieveBindingFor(InterfaceFoo::class).single()

        assertEquals("Foo2", instance.targetType!!.simpleName)
    }

    @Test
    fun retrieveBindingFor_chainBindingPresent_CanResolveToBoundImplementation(){
        _registry
                .bind(InterfaceFoo::class, Foo2::class)
                .bind(TypeWithDependency::class, TypeWithDependency::class)

        val instance = _registry.retrieveBindingFor(InterfaceFoo::class).single()

        assertEquals("Foo2", instance.targetType!!.simpleName)
    }

    @Test
    fun retrieveBindingFor_SupportsConditionalBindings(){
        _registry
                .bind<IConditionalBindingStub, ConditionalBindingImplementation1>(condition = {
                    x->x.whenInjectedInto(ConditionalBindingParent1::class)
                })
                .bind<IConditionalBindingStub, ConditionalBindingImplementation2>(condition = {
                    x->x.whenInjectedInto(ConditionalBindingParent2::class)
                })

        val bindings = _registry.retrieveBindingFor(IConditionalBindingStub::class)

        assertEquals(2, bindings.count())
    }
}