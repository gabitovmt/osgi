package org.example.hello.client;

import org.example.hello.Greeting;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class Client implements BundleActivator {

    @Override
    public void start(BundleContext ctx) {
        ServiceReference<Greeting> ref = ctx.getServiceReference(Greeting.class);
        ctx.getService(ref).sayHello();
    }

    @Override
    public void stop(BundleContext context) {
        // Не требуется
    }
}
