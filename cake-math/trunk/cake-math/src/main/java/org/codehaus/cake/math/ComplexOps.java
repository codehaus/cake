/*
 * Copyright 2008 Kasper Nielsen.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://cake.codehaus.org/LICENSE
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.codehaus.cake.math;

import java.io.Serializable;

import org.codehaus.cake.ops.Ops.ObjectToDouble;
import org.codehaus.cake.ops.Ops.Op;
import org.codehaus.cake.ops.Ops.Reducer;
/**
 * Various implementations of {@link ComplexPredicate}.
 * <p>
 * This class is normally best used via <tt>import static</tt>.
 *
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: ComplexOps.java 590 2008-03-14 08:16:12Z kasper $
 */
public final class ComplexOps {
    ///CLOVER:OFF
    /** Cannot instantiate. */
    private ComplexOps() {}
    ///CLOVER:ON

    
    final static ComplexAbsOp ABS_OP = new ComplexAbsOp();

    public static ObjectToDouble<Complex> abs() {
        return ABS_OP;
    } 

    static final class ComplexAbsOp implements ObjectToDouble<Complex>, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = -8583260658972887816L;

        public double op(Complex a) {
            return a.abs();
        }

        /** @return Preserves singleton property */
        private Object readResolve() {
            return ABS_OP;
        }
    }
     final static ComplexAddReducer ADD_REDUCER = new ComplexAddReducer();

     final static ComplexSubtractReducer SUBTRACT_REDUCER = new ComplexSubtractReducer();
    
     final static ComplexDivideReducer DIVIDE_REDUCER = new ComplexDivideReducer();
    
     final static ComplexMultiplyReducer MULTIPLY_REDUCER = new ComplexMultiplyReducer();

        public static Reducer<Complex> add() {
        return ADD_REDUCER;
    }
    
    public static Op<Complex, Complex> add(Complex add) {
        return new ComplexAddOp(add);
    }
    
    public static Reducer<Complex> divide() {
        return DIVIDE_REDUCER;
    }
    
    public static Op<Complex, Complex> divide(Complex divide) {
        return new ComplexDivideOp(divide);
    }
    
    public static Reducer<Complex> multiply() {
        return MULTIPLY_REDUCER;
    }
    
    public static Op<Complex, Complex> multiply(Complex multiply) {
        return new ComplexMultiplyOp(multiply);
    }
    
    public static Reducer<Complex> subtract() {
        return SUBTRACT_REDUCER;
    }
    
    public static Op<Complex, Complex> subtract(Complex substract) {
        return new ComplexSubtractOp(substract);
    }

    static final class ComplexSubtractReducer implements Reducer<Complex>, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = -8583260658972887816L;

        public Complex op(Complex a, Complex b) {
            return a.subtract(b);
        }

        /** @return Preserves singleton property */
        private Object readResolve() {
            return SUBTRACT_REDUCER;
        }
    }
    
    static final class ComplexAddReducer implements Reducer<Complex>, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = -830758681673022439L;

        public Complex op(Complex a, Complex b) {
            return a.add(b);
        }

        /** @return Preserves singleton property */
        private Object readResolve() {
            return ADD_REDUCER;
        }
    }
    
    
    static final class ComplexMultiplyReducer implements Reducer<Complex>, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = -130758681673022439L;

        public Complex op(Complex a, Complex b) {
            return a.multiply(b);
        }

        /** @return Preserves singleton property */
        private Object readResolve() {
            return MULTIPLY_REDUCER;
        }
    }
    
    static final class ComplexDivideReducer implements Reducer<Complex>, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = -330758681673022439L;

        public Complex op(Complex a, Complex b) {
            return a.divide(b);
        }

        /** @return Preserves singleton property */
        private Object readResolve() {
            return DIVIDE_REDUCER;
        }
    }
         static final class ComplexAddOp implements Op<Complex, Complex>, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = -6604604690824553900L;

        private final Complex add;

        public ComplexAddOp(Complex add) {
            this.add = add;
        }

        public Complex op(Complex a) {
            return a.add(add);
        }
    }
    static final class ComplexSubtractOp implements Op<Complex, Complex>, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = -23423423410L;

        private final Complex subtract;

        public ComplexSubtractOp(Complex subtract) {
            this.subtract = subtract;
        }

        public Complex op(Complex a) {
            return a.subtract(subtract);
        }
    }
    static final class ComplexDivideOp implements Op<Complex, Complex>, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = 661378303438906777L;

        private final Complex divide;

        public ComplexDivideOp(Complex divide) {
            this.divide = divide;
        }

        public Complex op(Complex a) {
            return a.divide(divide);
        }
    }

    static final class ComplexMultiplyOp implements Op<Complex, Complex>, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = 6099641660816235381L;

        private final Complex multiply;

        public ComplexMultiplyOp(Complex multiply) {
            this.multiply = multiply;
        }

        public Complex op(Complex a) {
            return a.multiply(multiply);
        }
    }}