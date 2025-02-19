package org.foo.shell;

public interface Binding {
    void start();
    void stop() throws InterruptedException;
}
