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
import java.math.BigInteger;

import org.codehaus.cake.ops.Ops.Op;
import org.codehaus.cake.ops.Ops.Reducer;
/**
 * Various implementations of {@link BigIntegerPredicate}.
 * <p>
 * This class is normally best used via <tt>import static</tt>.
 *
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: BigIntegerOps.java 590 2008-03-14 08:16:12Z kasper $
 */
public final class BigIntegerOps {
    ///CLOVER:OFF
    /** Cannot instantiate. */
    private BigIntegerOps() {}
    ///CLOVER:ON

    
    final static BigIntegerAbsOp ABS_OP = new BigIntegerAbsOp();

    public static Op<BigInteger, BigInteger> abs() {
        return ABS_OP;
    }

    static final class BigIntegerAbsOp implements Op<BigInteger, BigInteger>, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = -8583260658972887816L;

        public BigInteger op(BigInteger a) {
            return a.abs();
        }

        /** @return Preserves singleton property */
        private Object readResolve() {
            return ABS_OP;
        }
    }
     final static BigIntegerAddReducer ADD_REDUCER = new BigIntegerAddReducer();

     final static BigIntegerSubtractReducer SUBTRACT_REDUCER = new BigIntegerSubtractReducer();
    
     final static BigIntegerDivideReducer DIVIDE_REDUCER = new BigIntegerDivideReducer();
    
     final static BigIntegerMultiplyReducer MULTIPLY_REDUCER = new BigIntegerMultiplyReducer();

        public static Reducer<BigInteger> add() {
        return ADD_REDUCER;
    }
    
    public static Op<BigInteger, BigInteger> add(BigInteger add) {
        return new BigIntegerAddOp(add);
    }
    
    public static Reducer<BigInteger> divide() {
        return DIVIDE_REDUCER;
    }
    
    public static Op<BigInteger, BigInteger> divide(BigInteger divide) {
        return new BigIntegerDivideOp(divide);
    }
    
    public static Reducer<BigInteger> multiply() {
        return MULTIPLY_REDUCER;
    }
    
    public static Op<BigInteger, BigInteger> multiply(BigInteger multiply) {
        return new BigIntegerMultiplyOp(multiply);
    }
    
    public static Reducer<BigInteger> subtract() {
        return SUBTRACT_REDUCER;
    }
    
    public static Op<BigInteger, BigInteger> subtract(BigInteger substract) {
        return new BigIntegerSubtractOp(substract);
    }

    static final class BigIntegerSubtractReducer implements Reducer<BigInteger>, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = -8583260658972887816L;

        public BigInteger op(BigInteger a, BigInteger b) {
            return a.subtract(b);
        }

        /** @return Preserves singleton property */
        private Object readResolve() {
            return SUBTRACT_REDUCER;
        }
    }
    
    static final class BigIntegerAddReducer implements Reducer<BigInteger>, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = -830758681673022439L;

        public BigInteger op(BigInteger a, BigInteger b) {
            return a.add(b);
        }

        /** @return Preserves singleton property */
        private Object readResolve() {
            return ADD_REDUCER;
        }
    }
    
    
    static final class BigIntegerMultiplyReducer implements Reducer<BigInteger>, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = -130758681673022439L;

        public BigInteger op(BigInteger a, BigInteger b) {
            return a.multiply(b);
        }

        /** @return Preserves singleton property */
        private Object readResolve() {
            return MULTIPLY_REDUCER;
        }
    }
    
    static final class BigIntegerDivideReducer implements Reducer<BigInteger>, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = -330758681673022439L;

        public BigInteger op(BigInteger a, BigInteger b) {
            return a.divide(b);
        }

        /** @return Preserves singleton property */
        private Object readResolve() {
            return DIVIDE_REDUCER;
        }
    }
         static final class BigIntegerAddOp implements Op<BigInteger, BigInteger>, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = -6604604690824553900L;

        private final BigInteger add;

        public BigIntegerAddOp(BigInteger add) {
            this.add = add;
        }

        public BigInteger op(BigInteger a) {
            return a.add(add);
        }
    }
    static final class BigIntegerSubtractOp implements Op<BigInteger, BigInteger>, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = -23423423410L;

        private final BigInteger subtract;

        public BigIntegerSubtractOp(BigInteger subtract) {
            this.subtract = subtract;
        }

        public BigInteger op(BigInteger a) {
            return a.subtract(subtract);
        }
    }
    static final class BigIntegerDivideOp implements Op<BigInteger, BigInteger>, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = 661378303438906777L;

        private final BigInteger divide;

        public BigIntegerDivideOp(BigInteger divide) {
            this.divide = divide;
        }

        public BigInteger op(BigInteger a) {
            return a.divide(divide);
        }
    }

    static final class BigIntegerMultiplyOp implements Op<BigInteger, BigInteger>, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = 6099641660816235381L;

        private final BigInteger multiply;

        public BigIntegerMultiplyOp(BigInteger multiply) {
            this.multiply = multiply;
        }

        public BigInteger op(BigInteger a) {
            return a.multiply(multiply);
        }
    }}