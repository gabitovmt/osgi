package org.example.hello.osgi;

import org.example.hello.Greeting;
import org.example.hello.impl.GreetingImpl;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

    @Override
    public void start(BundleContext ctx) {
        ctx.registerService(Greeting.class.getName(), new GreetingImpl("service"), null);
    }

    @Override
    public void stop(BundleContext ctx) {
        // Не требуется
    }
}
