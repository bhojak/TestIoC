package ioc

class DependencyInfo(var contract: Class<*>, var implementation: Class<*>) {
    var isBuildImmediately = true
}