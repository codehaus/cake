package org.codehaus.cake.concurrent;

import java.util.concurrent.Future;

public interface RunnableFuture<V> extends Runnable, Future<V>{

}
