package test.activation



import ioc.activation.ConstructorSelector
import junit.framework.Assert.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import test.data2.Bar
import test.data2.TypeWithTwoConstructors
import test.data2.TypeWithTwoEqualSizedConstructors


class ConstructorSelectorTests  {

    private lateinit var _selector: ConstructorSelector

    @BeforeEach
    fun setUp(){
        _selector = ConstructorSelector()
    }

    @Test
    fun typeWithNoConstructors_DefaultCtorSelected() {
        val ctor = _selector.select(Bar::class)

        assertEquals(0, ctor.parameters.count())
    }

    @Test
    fun typeWithTwoConstructors_LargestCtorSelected() {
        val ctor = _selector.select(TypeWithTwoConstructors::class)

        assertEquals(2, ctor.parameters.count())
    }

    @Test
    fun typeWithTwoEqualSizedConstructors_FirstCtorSelected() {
        val ctor = _selector.select(TypeWithTwoEqualSizedConstructors::class)

        assertEquals("foo", ctor.parameters[0].name)
    }
}
