package org.codehaus.cake.internal.cache.view;

import org.codehaus.cake.internal.cache.processor.CacheProcessor;
import org.codehaus.cake.internal.cache.view.util.QueryStack;
import org.codehaus.cake.util.ops.Ops.Predicate;

public class AbstractView {
    final Object command;
    final CacheProcessor executor;
    final Predicate filter;
    final AbstractView parent;
    final int stackDepth;
    final boolean isCommandArray;

    public AbstractView(AbstractView parent, Object command) {
        if (command == null) {
            throw new NullPointerException("command is null");
        }
        this.command = command;
        this.parent = parent;
        this.filter = parent.filter;
        executor = parent.executor;
        stackDepth = parent.stackDepth + 1;
        isCommandArray = false;
    }

    public AbstractView(AbstractView parent, Object[] command) {
        if (command == null) {
            throw new NullPointerException("command is null");
        }
        this.command = command;
        this.parent = parent;
        this.filter = parent.filter;
        executor = parent.executor;
        stackDepth = parent.stackDepth + command.length;
        isCommandArray = true;
    }

    public AbstractView(CacheProcessor executor, Predicate filter) {
        if (executor == null) {
            throw new NullPointerException("executor is null");
        }
        stackDepth = 0;
        this.filter = filter;
        this.command = null;
        this.parent = null;
        this.executor = executor;
        isCommandArray = false;
    }

    private boolean isRoot() {
        return parent == null;
    }

    private AbstractView pushThisReturnParent(QueryStack stack) {
        if (isCommandArray) {

        } else {
            stack.push(command);
        }
        return parent;
    }

    Object execute(Object command) {
        QueryStack stack = new QueryStack(stackDepth + 1);
        stack.push(command);
        AbstractView v = this;
        while (!v.isRoot()) {
            v = v.pushThisReturnParent(stack);
        }
        return executor.executeView(filter, this, stack);
    }

    Object execute(Object command1, Object command2) {
        QueryStack stack = new QueryStack(stackDepth + 2);
        stack.push(command2).push(command1);
        AbstractView v = this;
        while (!v.isRoot()) {
            v = v.pushThisReturnParent(stack);
        }
        return executor.executeView(filter, this, stack);
    }

    public long size() {
        return ((Number) execute(Long.class)).longValue();
    }
}
