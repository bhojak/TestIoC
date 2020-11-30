package ioc

import java.lang.reflect.InvocationTargetException
import java.util.ArrayList
import java.util.HashMap

object Container {

    private val cache = HashMap<Class<*>, Any>()
    private val mappings = HashMap<Class<*>, Class<*>>()
    private val buildImmediately = HashMap<Class<*>, Boolean>()
    private var log: Log? = null
    private var initialized = false

    init {
        cache.clear()
        mappings.clear()
        initialized = false
    }

    @Throws(ClassNotFoundException::class)
    fun setup(containerSetup: ContainerSetup) {
        for (dependencyInfo in containerSetup.definition) {
            val contract = dependencyInfo.contract
            buildImmediately[contract] = dependencyInfo.isBuildImmediately
            Container[contract] = dependencyInfo.implementation
        }
    }

    fun setLog(log: Log) {
        Container.log = log
    }

    @Throws(ClassNotFoundException::class)
    fun init() {
        initialized = true
        for (clz in mappings.keys) {
            if (buildImmediately[clz] == null || buildImmediately[clz] === false) {
                continue
            }
            if (cache.containsKey(clz)) {
                continue
            }
            cache[clz] = getObject(clz, ArrayList())
        }
        log = getInstance(Log::class.java)
        if (log == null) {
            throw IllegalStateException("No class implements " + Log::class.java.name + " found. Please add default logger.\n" + dump())
        }
        dump()
    }

    @Throws(ClassNotFoundException::class)
    operator fun set(contract: Class<*>, className: String) {
        if (mappings.containsKey(contract)) {
            throw IllegalStateException(String.format("Definition for class %s already exists", contract.name))
        }
        mappings[contract] = Class.forName(className)
    }

    @Throws(ClassNotFoundException::class)
    operator fun set(contract: Class<*>, clz: Class<*>) {
        mappings[contract] = clz
    }

    operator fun set(contract: Class<*>, `object`: Any) {
        mappings[contract] = `object`.javaClass
        cache[contract] = `object`
    }

    @Throws(ClassNotFoundException::class)
    fun <T> getInstance(name: String): T? {
        if (!initialized) {
            throw IllegalStateException("Container not initialized")
        }
        return cache[Class.forName(name)] as T
    }

    fun <T> getInstance(clz: Class<T>): T? {
        if (!initialized) {
            throw IllegalStateException("Container not initialized")
        }
        return cache[clz] as T
    }

    private fun getRecursivePath(recursiveInvocationPath: List<String>): String {
        val sb = StringBuilder()
        val separator = "->"
        for (name in recursiveInvocationPath) {
            sb.append(name).append(separator)
        }
        return sb.substring(0, sb.length - separator.length)
    }

    @Throws(ClassNotFoundException::class)
    private fun getObject(clz: Class<*>, recursiveInvocationPath: MutableList<String>): Any {
        log(String.format("Building object of %s", clz.name))

        checkCircularDependency(clz, recursiveInvocationPath)
        recursiveInvocationPath.add(clz.name)

        val obj = getInstance(clz)
        if (obj != null) {
            return obj
        }
        val objectImp = mappings[clz] ?: throw ClassNotFoundException()
        log(String.format("Apply mapping %s => %s", clz.name, objectImp.name))
        val constructors = objectImp.constructors

        for (constructor in constructors) {
            try {
                log(String.format("Evaluate constructor %s", constructor.name))
                if (constructor.parameterTypes.isEmpty()) {
                    // parameterless
                    log(String.format("No parameters needed building..."))
                    return constructor.newInstance()
                } else {
                    // get params recursively
                    val paramInstances = ArrayList<Any>()
                    log(String.format("%d parameters needed", constructor.parameterTypes.size))
                    for (constructorParamClz in constructor.parameterTypes) {
                        log(String.format("Looking for parameter %s", constructorParamClz.name))
                        val paramInstance = getObject(constructorParamClz, ArrayList(recursiveInvocationPath))
                        paramInstances.add(paramInstance)
                        log(String.format("Parameter of type %s found %s", constructorParamClz.name, paramInstance))
                    }
                    log(String.format("All parameters ready. Building %s ", clz.name))
                    return constructor.newInstance(*paramInstances.toTypedArray())
                }
            } catch (e: IllegalArgumentException) {
                log(e)
            } catch (e: InstantiationException) {
                log(e)
            } catch (e: IllegalAccessException) {
                log(e)
            } catch (e: InvocationTargetException) {
                log(e)
            }

        }
        throw ClassNotFoundException(String.format("Can't find class %s for %s", clz.name, getRecursivePath(recursiveInvocationPath)))
    }

    private fun checkCircularDependency(clz: Class<*>, recursiveInvocationPath: List<String>) {
        if (recursiveInvocationPath.contains(clz.name)) {
            throw IllegalStateException("Circular dependencies " + getRecursivePath(recursiveInvocationPath))
        }
    }

    fun clearCache() {
        cache.clear()
    }

    fun clear() {
        cache.clear()
        mappings.clear()
        initialized = false
    }

    @Throws(ClassNotFoundException::class)
    fun getClassFor(clz: Class<*>): Class<*> {
        if (mappings.containsKey(clz)) {
            return mappings[clz] as Class<*>
        }
        throw ClassNotFoundException()
    }

    @Throws(ClassNotFoundException::class)
    fun getClassName(className: String): String {
        return getClassFor(Class.forName(className)).name
    }

    @Throws(ClassNotFoundException::class)
    fun getClassName(clz: Class<*>): String {
        return getClassFor(clz).name
    }

    private fun log(message: String) {
        if (log != null) {
            log!!.d(message)
        }
    }

    private fun log(exception: Exception) {
        if (log != null) {
            log!!.e(exception)
        }
    }

    private fun dump(): String {
        val sb = StringBuilder()
        sb.append("CACHE:\n")
        for (key in cache.keys) {
            sb.append("Contract: ").append(key.name).append(" -> ").append(
                    if (cache[key] == null)
                        "null"
                    else cache[key]!!.javaClass.name)
                    .append("\n")
        }
        sb.append("MAPPINGS:\n")
        for (key in mappings.keys) {
            sb.append("Contract: ").append(key.name).append(" -> ").append(if (mappings[key] == null) "null" else mappings[key]!!.name)
                    .append("\n")
        }
        return sb.toString()
    }
}