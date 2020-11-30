package ioc

import java.util.ArrayList

object IoCConfiguration {

    @Synchronized fun prepareContainer() {
        try {
            Container.setLog(IoCLogger())
            val definitions = ArrayList<DependencyInfo>()

            val containerSetup = ContainerSetup()
            containerSetup.setDefinitions(definitions)
            Container.setup(containerSetup)
            Container.init()
        } catch (e: ClassNotFoundException) {
            throw RuntimeException(e)
        }

    }
}