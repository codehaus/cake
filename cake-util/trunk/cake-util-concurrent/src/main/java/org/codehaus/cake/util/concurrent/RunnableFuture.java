package org.codehaus.cake.util.concurrent;

import java.util.concurrent.Future;

interface RunnableFuture<V> extends Runnable, Future<V>{

}
