package test

import ioc.CircularDependencyException
import ioc.IoCContainer
import ioc.registration.Lifecycle
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import test.data2.*
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class IoCContainerTests {

    private lateinit var _container: IoCContainer

    @BeforeEach
    fun setUp() {
        _container = IoCContainer()
        _container.registrations.autoDiscovery = false
        _container.registrations.bind(InterfaceFoo::class, Foo::class)
        _container.registrations.bind(InterfaceBar::class, Bar::class, lifecycle = Lifecycle.Singleton)
    }

    @Test
    fun resolveT_ResolvesTypesFromRegistry() {
        val instance = _container.resolve<InterfaceFoo>()
        assertNotNull(instance)
    }

    @Test
    fun resolve_ResolvesTypesFromRegistry() {
        val instance = _container.resolve(InterfaceFoo::class)
        assertNotNull(instance)
    }

    @Test
    fun resolve_ResolvesTypesFromDelegates() {
        val foo = Foo()
        _container.registrations.bind(Foo::class, { foo })

        val instance = _container.resolve(Foo::class)

        assertEquals(foo, instance)
    }

    @Test
    fun resolve_TypeBoundAsSingleton_ResolvesSameInstanceTwice() {
        val instance1 = _container.resolve(InterfaceBar::class)
        val instance2 = _container.resolve(InterfaceBar::class)
        assertEquals(instance1, instance2)
    }

    @Test
    fun resolve_TypeWithCircularDependency_Throws() {
        _container.registrations.bind(TypeWithACircularDep::class)
        _container.registrations.bind(TypeWithACircularDep2::class)
        assertThrows<CircularDependencyException> { _container.resolve(TypeWithACircularDep::class) }
    }

    @Test
    fun resolve_SupportsConditionalBindingsBasedOnInjectionTarget(){
        _container.registrations
                .bindSelf<ConditionalBindingParent1>()
                .bindSelf<ConditionalBindingParent2>()
                .bind<IConditionalBindingStub, ConditionalBindingImplementation1>(condition = {
                    x->x.whenInjectedInto(ConditionalBindingParent1::class)
                })
                .bind<IConditionalBindingStub, ConditionalBindingImplementation2>(condition = {
                    x->x.whenInjectedInto(ConditionalBindingParent2::class)
                })

        val instance1 =  _container.resolve<ConditionalBindingParent1>()
        val instance2 =  _container.resolve<ConditionalBindingParent2>()

        assertEquals(ConditionalBindingImplementation1::class, instance1.injected::class)
        assertEquals(ConditionalBindingImplementation2::class, instance2.injected::class)
    }

    @Test
    fun resolve_supportsOnlyWhenBindings(){
        _container.registrations
                .bindSelf<ConditionalBindingParent1>()
                .bindSelf<ConditionalBindingParent2>()
                .bind<IConditionalBindingStub, ConditionalBindingImplementation1>()
                .bind<IConditionalBindingStub, ConditionalBindingImplementation2>(condition = {
                    x -> x.onlyWhen { ctx -> ctx.rootType == ConditionalBindingParent2::class }
                })

        val instance1 =  _container.resolve<ConditionalBindingParent1>()
        val instance2 =  _container.resolve<ConditionalBindingParent2>()

        assertEquals(ConditionalBindingImplementation1::class, instance1.injected::class)
        assertEquals(ConditionalBindingImplementation2::class, instance2.injected::class)
    }

    @Test
    fun resolve_MultipleBindingsExistThatAllMatch_LastOneInWins(){
        _container.registrations
                .bind<IConditionalBindingStub, ConditionalBindingImplementation1>()
                .bind<IConditionalBindingStub, ConditionalBindingImplementation2>()

        val instance =  _container.resolve<IConditionalBindingStub>()

        assertEquals(ConditionalBindingImplementation2::class, instance::class)
    }
}

