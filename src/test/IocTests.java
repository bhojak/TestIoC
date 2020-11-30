package  test;

import ioc.Container;
import ioc.IoCLogger;
import ioc.Log;
import junit.framework.TestCase;
import test.data.ClassA;
import test.data.ClassB;
import test.data.InterfaceA;
import test.data.InterfaceB;

public class IocTests extends TestCase {

    @Override
    protected void setUp() {
        Container.INSTANCE.clearCache();
        Container.INSTANCE.clear();
    }

    public void testContainerNotInitializedGetInstanceByType() {
        try {
            Container.INSTANCE.getInstance(InterfaceA.class);
            fail();
        } catch (RuntimeException e) {
            assertEquals(IllegalStateException.class, e.getClass());
        }
    }

    public void testContainerNotInitializedGetInstanceByName() throws ClassNotFoundException {
        try {
            Container.INSTANCE.getInstance("InterfaceA");
            fail();
        } catch (RuntimeException e) {
            assertEquals(IllegalStateException.class, e.getClass());
        }
    }

    public void testNoDefaultLoggerSet() throws ClassNotFoundException {
        try {
            Container.INSTANCE.init();
            Container.INSTANCE.getInstance(InterfaceA.class);
            fail();
        } catch (RuntimeException e) {
            assertEquals(IllegalStateException.class, e.getClass());
        }
    }

    public void testNoClassDeclaration() throws ClassNotFoundException {
        Container.INSTANCE.set(Log.class, new IoCLogger());
        Container.INSTANCE.init();
        Object interfaceA = Container.INSTANCE.getInstance(InterfaceA.class);
        assertNull(interfaceA);
    }

    public void testGetInstanceSimpleClassByType() throws ClassNotFoundException {
        Container.INSTANCE.set(Log.class, new IoCLogger());
        Container.INSTANCE.set(InterfaceA.class, ClassA.class);
        Container.INSTANCE.init();
        Object interfaceA = Container.INSTANCE.getInstance(InterfaceA.class);
        assertNull(interfaceA);
    }

    public void testGetInstanceSimpleClassByName() throws ClassNotFoundException {
        Container.INSTANCE.set(Log.class, new IoCLogger());
        Container.INSTANCE.set(InterfaceA.class, ClassA.class);
        Container.INSTANCE.init();
        Object interfaceA = Container.INSTANCE.getInstance(InterfaceA.class);
        assertNull(interfaceA);
    }

    public void testGetInstanceRecursive() throws ClassNotFoundException {
        Container.INSTANCE.set(Log.class, new IoCLogger());
        Container.INSTANCE.set(InterfaceA.class, ClassA.class);
        Container.INSTANCE.set(InterfaceB.class, ClassB.class);

        Container.INSTANCE.init();
        Object interfaceB = Container.INSTANCE.getInstance("test.data.InterfaceB");
        assertNull(interfaceB);
    }

    public void testGetInstanceRecursiveNotFound() throws ClassNotFoundException {
        Container.INSTANCE.set(Log.class, new IoCLogger());
        Container.INSTANCE.set(InterfaceB.class, ClassB.class);

        Container.INSTANCE.init();
        Object interfaceB = Container.INSTANCE.getInstance(InterfaceB.class);
        assertNull(interfaceB);
    }
}