package test

import ioc.IoCContainer
import ioc.registration.Lifecycle
import org.junit.jupiter.api.Test
import test.data2.*
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class UseCaseExamples {

    @Test
    fun `Explicit bindings`() {
        val container = IoCContainer()
        container.registrations
                .bind(Foo::class)
                .bind(Bar::class)

        val foo = container.resolve<Foo>()
        val foo2 = container.resolve<Bar>()

        assertNotNull(foo)
        assertNotNull(foo2)
    }

    @Test
    fun `Generic bindings`() {
        val container = IoCContainer()
        container.registrations
                .bind<InterfaceFoo, Foo>()
                .bind<Bar, Bar>()

        val foo = container.resolve<Foo>()
        val foo2 = container.resolve<Bar>()

        assertNotNull(foo)
        assertNotNull(foo2)
    }

    @Test
    fun `Bindings to factory functions`() {
        val container = IoCContainer()
        container.registrations
                .bind<InterfaceFoo>(createFoo())
                .bind<InterfaceBar>({ Bar() })

        val foo = container.resolve<Foo>()
        val foo2 = container.resolve<Bar>()

        assertNotNull(foo)
        assertNotNull(foo2)
    }

    private fun createFoo() = { Foo() }

    @Test
    fun `Scan for auto-registration`() {
        val container = IoCContainer()
        container.registrations.scan
                .fromPackageContaining<InterfaceFoo> { x -> x.bindAllInterfaces()  }
                .fromPackageContaining<InterfaceFoo> { x -> x.bindClassesToSelf()  }

        val bar = container.resolve<Bar>()

        assertNotNull(bar)
    }

    @Test
    fun `Contextual bindings`() {
        val container = IoCContainer()

        container.registrations
                .bindSelf<ConditionalBindingParent1>()
                .bindSelf<ConditionalBindingParent2>()
                .bind<IConditionalBindingStub, ConditionalBindingImplementation1>(condition = {
                    x->x.whenInjectedInto(ConditionalBindingParent1::class)
                })
                .bind<IConditionalBindingStub, ConditionalBindingImplementation2>(condition = {
                    x->x.whenInjectedInto(ConditionalBindingParent2::class)
                })

        val instance1 =  container.resolve<ConditionalBindingParent1>()
        val instance2 =  container.resolve<ConditionalBindingParent2>()

        assertEquals(ConditionalBindingImplementation1::class, instance1.injected::class)
        assertEquals(ConditionalBindingImplementation2::class, instance2.injected::class)
    }

    @Test
    fun `Freeform contextual bindings using onlyWhen`(){
        val container = IoCContainer()

        container.registrations
                .bindSelf<ConditionalBindingParent1>()
                .bindSelf<ConditionalBindingParent2>()
                .bind<IConditionalBindingStub, ConditionalBindingImplementation1>()
                .bind<IConditionalBindingStub, ConditionalBindingImplementation2>(condition = {
                    x -> x.onlyWhen { ctx -> ctx.rootType == ConditionalBindingParent2::class }
                })

        val instance1 =  container.resolve<ConditionalBindingParent1>()
        val instance2 =  container.resolve<ConditionalBindingParent2>()

        assertEquals(ConditionalBindingImplementation1::class, instance1.injected::class)
        assertEquals(ConditionalBindingImplementation2::class, instance2.injected::class)
    }


    @Test
    fun `Configure a singleton`(){
        val container = IoCContainer()
        container.registrations.bind(InterfaceBar::class, Bar::class, lifecycle = Lifecycle.Singleton)

        val instance1 = container.resolve(InterfaceBar::class)
        val instance2 = container.resolve(InterfaceBar::class)

        assertEquals(instance1, instance2)
    }

    @Test
    fun `Recommended approach`(){
        val container = IoCContainer()

        // Scan for everything
        container.registrations.scan
                .fromPackageContaining<InterfaceFoo> { x -> x.bindClassesAndInterfaces() }

        // Register factories for special cases
        container.registrations
                .bind<InterfaceFoo>(createFoo(), lifecycle = Lifecycle.Singleton)
                .bind<InterfaceBar>({ Bar() })

        // Override bindings when you need to switch out implementations
        container.registrations
                .bind<IConditionalBindingStub, ConditionalBindingImplementation1>(condition = {
                    x->x.whenInjectedInto(ConditionalBindingParent1::class)
                })
                .bind<IConditionalBindingStub, ConditionalBindingImplementation2>(condition = {
                    x->x.whenInjectedInto(ConditionalBindingParent2::class)
                })

        val bar = container.resolve<Bar>()

        assertNotNull(bar)
    }
}