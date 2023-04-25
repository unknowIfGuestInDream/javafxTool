package groovy

import com.tlcsdm.core.util.groovy.IFoo

class Foo implements IFoo {
    Object run(Object foo) { return 2 + 2 > 1 }
}
