/*
 * Copyright 2008 Kasper Nielsen.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.codehaus.cake.ops;

import java.io.PrintStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Queue;

import org.codehaus.cake.ops.Ops.Op;
import org.codehaus.cake.ops.Ops.Procedure;

/**
 * Various implementations of {@link Procedure}.
 * <p>
 * This class is normally best used via <tt>import static</tt>.
 * 
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: Procedures.java 587 2008-02-06 08:21:44Z kasper $
 */
public final class Procedures {

    /**
     * A Procedure that does nothing.
     */
    public final static Procedure IGNORE_PROCEDURE = new NoopProcedure();

    /**
     * A Procedure that prints the argument object to {@link System#out} using its
     * {@link Object#toString()} and {@link PrintStream#print(String)}.
     */
    public final static Procedure SYS_OUT_PRINT_PROCEDURE = new SystemOutPrintProcedure();

    /**
     * A Procedure that prints the argument object to {@link System#out} using its
     * {@link Object#toString()} and {@link PrintStream#println(String)} .
     */
    public final static Procedure SYS_OUT_PRINTLN_PROCEDURE = new SystemOutPrintlnProcedure();

    /** Cannot instantiate. */
    // /CLOVER:OFF
    private Procedures() {}
    // /CLOVER:ON
    /**
     * Returns a Procedure that does nothing.
     * 
     * @return a Procedure that does nothing.
     * @param <T>
     *            the types of elements accepted by the specified Collection
     */
    public static <T> Procedure<T> ignore() {
        return IGNORE_PROCEDURE;
    }
//
//    public static <F, T> Procedure<F> mapAndApply(final Op<F, T> mapper,
//            Procedure<? super T> procedure) {
//        return new MapAndApplyPredicate<F, T>(mapper, procedure);
//    }

    /**
     * Returns a Procedure that calls {@link PrintStream#print(boolean)} on {@link System#out}.
     * 
     * @return an eventhandler that prints all processed elements to {@link System#out}
     * @param <E>
     *            the types of elements accepted by the procedure
     */
    public static <E> Procedure<E> systemOutPrint() {
        return SYS_OUT_PRINT_PROCEDURE;
    }

    /**
     * Returns a Procedure that calls {@link PrintStream#println(boolean)} on {@link System#out}.
     * 
     * @return an eventhandler that prints all processed elements to {@link System#out}
     * @param <E>
     *            the types of elements accepted by the procedure
     */
    public static <E> Procedure<E> systemOutPrintln() {
        return SYS_OUT_PRINTLN_PROCEDURE;
    }

//    /**
//     * A Predicate that first applies the specified mapper to the argument before evaluating the
//     * specified predicate.
//     */
//    final static class MapAndApplyPredicate<F, T> implements Procedure<F>, Serializable {
//
//        /** serialVersionUID. */
//        private static final long serialVersionUID = -6292758840373110577L;
//
//        /** The mapper used to map the element. */
//        private final Op<F, T> mapper;
//
//        /** The procedure to to apply the mapped value on. */
//        private final Procedure<? super T> procedure;
//
//        /**
//         * Creates a new MapAndEvaluatePredicate.
//         * 
//         * @param mapper
//         *            the mapper used to first map the argument
//         * @param procedure
//         *            the predicate used to evaluate the mapped argument
//         */
//        public MapAndApplyPredicate(Op<F, T> mapper, Procedure<? super T> procedure) {
//            if (mapper == null) {
//                throw new NullPointerException("mapper is null");
//            } else if (procedure == null) {
//                throw new NullPointerException("procedure is null");
//            }
//            this.procedure = procedure;
//            this.mapper = mapper;
//        }
//
//        /**
//         * Returns the mapper that will map the object before applying the predicate on it.
//         * 
//         * @return the mapper that will map the object before applying the predicate on it
//         */
//        public Op<F, T> getMapper() {
//            return mapper;
//        }
//
//        /**
//         * Returns the Procedure we are testing against.
//         * 
//         * @return the Procedure we are testing against.
//         */
//        public Procedure<? super T> getProcedure() {
//            return procedure;
//        }
//
//        public void op(F element) {
//            procedure.op(mapper.op(element));
//        }
//    }

    /**
     * A Procedure that does nothing.
     */
    final static class NoopProcedure implements Procedure, Serializable {

        /** Default <code>serialVersionUID</code>. */
        private static final long serialVersionUID = 3258129137502925875L;

        /** Creates a new NoopProcedure. */
        NoopProcedure() {}

        /** {@inheritDoc} */
        public void op(Object t) {}

        /** @return Preserves singleton property */
        private Object readResolve() {
            return IGNORE_PROCEDURE;
        }
    }

    /**
     * A Procedure that calls {@link PrintStream#println(boolean)} on {@link System#out}.
     */
    final static class SystemOutPrintlnProcedure implements Procedure, Serializable {

        /** Default <code>serialVersionUID</code>. */
        private static final long serialVersionUID = 2524986731849709296L;

        /** Creates a new NoopProcedure. */
        SystemOutPrintlnProcedure() {}

        /** {@inheritDoc} */
        public void op(Object t) {
            System.out.println(t);
        }

        /** @return Preserves singleton property */
        private Object readResolve() {
            return SYS_OUT_PRINTLN_PROCEDURE;
        }
    }

    /**
     * A Procedure that calls {@link PrintStream#print(boolean)} on {@link System#out}.
     */
    final static class SystemOutPrintProcedure implements Procedure, Serializable {

        /** Default <code>serialVersionUID</code>. */
        private static final long serialVersionUID = 5504109727345743170L;

        /** Creates a new NoopProcedure. */
        SystemOutPrintProcedure() {}

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
