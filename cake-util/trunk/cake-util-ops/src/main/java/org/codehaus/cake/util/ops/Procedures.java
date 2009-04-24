/*
 * Copyright 2008, 2009 Kasper Nielsen.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
package org.codehaus.cake.util.ops;

import java.io.PrintStream;
import java.io.Serializable;

import org.codehaus.cake.util.ops.Ops.Procedure;

/**
 * Various implementations of {@link Procedure}.
 * <p>
 * This class is normally best used via <tt>import static</tt>.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id$
 */
public final class Procedures {

    /** A Procedure that does nothing. */
    public static final Procedure IGNORE_PROCEDURE = new NoopProcedure();

    /**
     * A Procedure that prints the argument object to {@link System#out} using its {@link Object#toString()} and
     * {@link PrintStream#print(String)}.
     */
    public static final Procedure SYS_ERR_PRINT_PROCEDURE = new SystemErrPrintProcedure();

    /**
     * A Procedure that prints the argument object to {@link System#err} using its {@link Object#toString()} and
     * {@link PrintStream#println(String)} .
     */
    public static final Procedure SYS_ERR_PRINTLN_PROCEDURE = new SystemErrPrintlnProcedure();

    /**
     * A Procedure that prints the argument object to {@link System#out} using its {@link Object#toString()} and
     * {@link PrintStream#print(String)}.
     */
    public static final Procedure SYS_OUT_PRINT_PROCEDURE = new SystemOutPrintProcedure();

    /**
     * A Procedure that prints the argument object to {@link System#out} using its {@link Object#toString()} and
     * {@link PrintStream#println(String)} .
     */
    public static final Procedure SYS_OUT_PRINTLN_PROCEDURE = new SystemOutPrintlnProcedure();

    /** Cannot instantiate. */
    private Procedures() {
    }

    /**
     * Returns a Procedure that does nothing.
     * <p>
     * Using this method is likely to have comparable cost to using the like-named field. (Unlike this method, the field
     * does not provide type safety.
     * 
     * @return a Procedure that does nothing.
     * @param <T>
     *            the types of elements accepted by the specified Collection
     */
    public static <T> Procedure<T> ignore() {
        return IGNORE_PROCEDURE;
    }

    /**
     * Returns a Procedure that calls {@link PrintStream#print(boolean)} on {@link System#err}.
     * <p>
     * Using this method is likely to have comparable cost to using the like-named field. (Unlike this method, the field
     * does not provide type safety.
     * 
     * @return a procedure that prints all processed elements to {@link System#err}
     * @param <E>
     *            the types of elements accepted by the procedure
     */
    public static <E> Procedure<E> systemErrPrint() {
        return SYS_ERR_PRINT_PROCEDURE;
    }

    /**
     * Returns a Procedure that calls {@link PrintStream#println(boolean)} on {@link System#err}.
     * <p>
     * Using this method is likely to have comparable cost to using the like-named field. (Unlike this method, the field
     * does not provide type safety.
     * 
     * @return a procedure that prints all processed elements to {@link System#err}
     * @param <E>
     *            the types of elements accepted by the procedure
     */
    public static <E> Procedure<E> systemErrPrintln() {
        return SYS_ERR_PRINTLN_PROCEDURE;
    }

    /**
     * Returns a Procedure that calls {@link PrintStream#print(boolean)} on {@link System#out}.
     * <p>
     * Using this method is likely to have comparable cost to using the like-named field. (Unlike this method, the field
     * does not provide type safety.
     * 
     * @return a procedure that prints all processed elements to {@link System#out}
     * @param <E>
     *            the types of elements accepted by the procedure
     */
    public static <E> Procedure<E> systemOutPrint() {
        return SYS_OUT_PRINT_PROCEDURE;
    }

    /**
     * Returns a Procedure that calls {@link PrintStream#println(boolean)} on {@link System#out}.
     * <p>
     * Using this method is likely to have comparable cost to using the like-named field. (Unlike this method, the field
     * does not provide type safety.
     * 
     * @return a procedure that prints all processed elements to {@link System#out}
     * @param <E>
     *            the types of elements accepted by the procedure
     */
    public static <E> Procedure<E> systemOutPrintln() {
        return SYS_OUT_PRINTLN_PROCEDURE;
    }

    /** A Procedure that does nothing. */
    static final class NoopProcedure implements Procedure, Serializable {

        /** Default <code>serialVersionUID</code>. */
        private static final long serialVersionUID = 1L;

        /** {@inheritDoc} */
        public void op(Object t) {
        }

        /** @return Preserves singleton property */
        private Object readResolve() {
            return IGNORE_PROCEDURE;
        }
    }

    /** A Procedure that calls {@link PrintStream#println(boolean)} on {@link System#err}. */
    static final class SystemErrPrintlnProcedure implements Procedure, Serializable {

        /** Default <code>serialVersionUID</code>. */
        private static final long serialVersionUID = 1L;

        /** {@inheritDoc} */
        public void op(Object t) {
            System.err.println(t);
        }

        /** @return Preserves singleton property */
        private Object readResolve() {
            return SYS_ERR_PRINTLN_PROCEDURE;
        }
    }

    /** A Procedure that calls {@link PrintStream#print(boolean)} on {@link System#err}. */
    static final class SystemErrPrintProcedure implements Procedure, Serializable {

        /** Default <code>serialVersionUID</code>. */
        private static final long serialVersionUID = 1L;

        /** {@inheritDoc} */
        public void op(Object t) {
            System.err.print(t);
        }

        /** @return Preserves singleton property */
        private Object readResolve() {
            return SYS_ERR_PRINT_PROCEDURE;
        }
    }

    /** A Procedure that calls {@link PrintStream#println(boolean)} on {@link System#out}. */
    static final class SystemOutPrintlnProcedure implements Procedure, Serializable {

        /** Default <code>serialVersionUID</code>. */
        private static final long serialVersionUID = 1L;

        /** {@inheritDoc} */
        public void op(Object t) {
            System.out.println(t);
        }

        /** @return Preserves singleton property */
        private Object readResolve() {
            return SYS_OUT_PRINTLN_PROCEDURE;
        }
    }

    /** A Procedure that calls {@link PrintStream#print(boolean)} on {@link System#out}. */
    static final class SystemOutPrintProcedure implements Procedure, Serializable {

        /** Default <code>serialVersionUID</code>. */
        private static final long serialVersionUID = 1L;

        /** {@inheritDoc} */
        public void op(Object t) {
            System.out.print(t);
        }

        /** @return Preserves singleton property */
        private Object readResolve() {
            return SYS_OUT_PRINT_PROCEDURE;
        }
    }
}
