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
import java.math.BigDecimal;

import org.codehaus.cake.ops.Ops.Op;
import org.codehaus.cake.ops.Ops.Reducer;
/**
 * Various implementations of {@link BigDecimalPredicate}.
 * <p>
 * This class is normally best used via <tt>import static</tt>.
 *
 * @author <a href="mailto:kasper@codehaus.org">Kasper Nielsen</a>
 * @version $Id: BigDecimalOps.java 590 2008-03-14 08:16:12Z kasper $
 */
public final class BigDecimalOps {
    ///CLOVER:OFF
    /** Cannot instantiate. */
    private BigDecimalOps() {}
    ///CLOVER:ON

    
    final static BigDecimalAbsOp ABS_OP = new BigDecimalAbsOp();

    public static Op<BigDecimal, BigDecimal> abs() {
        return ABS_OP;
    }

    static final class BigDecimalAbsOp implements Op<BigDecimal, BigDecimal>, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = -8583260658972887816L;

        public BigDecimal op(BigDecimal a) {
            return a.abs();
        }

        /** @return Preserves singleton property */
        private Object readResolve() {
            return ABS_OP;
        }
    }
     final static BigDecimalAddReducer ADD_REDUCER = new BigDecimalAddReducer();

     final static BigDecimalSubtractReducer SUBTRACT_REDUCER = new BigDecimalSubtractReducer();
    
     final static BigDecimalDivideReducer DIVIDE_REDUCER = new BigDecimalDivideReducer();
    
     final static BigDecimalMultiplyReducer MULTIPLY_REDUCER = new BigDecimalMultiplyReducer();

        public static Reducer<BigDecimal> add() {
        return ADD_REDUCER;
    }
    
    public static Op<BigDecimal, BigDecimal> add(BigDecimal add) {
        return new BigDecimalAddOp(add);
    }
    
    public static Reducer<BigDecimal> divide() {
        return DIVIDE_REDUCER;
    }
    
    public static Op<BigDecimal, BigDecimal> divide(BigDecimal divide) {
        return new BigDecimalDivideOp(divide);
    }
    
    public static Reducer<BigDecimal> multiply() {
        return MULTIPLY_REDUCER;
    }
    
    public static Op<BigDecimal, BigDecimal> multiply(BigDecimal multiply) {
        return new BigDecimalMultiplyOp(multiply);
    }
    
    public static Reducer<BigDecimal> subtract() {
        return SUBTRACT_REDUCER;
    }
    
    public static Op<BigDecimal, BigDecimal> subtract(BigDecimal substract) {
        return new BigDecimalSubtractOp(substract);
    }

    static final class BigDecimalSubtractReducer implements Reducer<BigDecimal>, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = -8583260658972887816L;

        public BigDecimal op(BigDecimal a, BigDecimal b) {
            return a.subtract(b);
        }

        /** @return Preserves singleton property */
        private Object readResolve() {
            return SUBTRACT_REDUCER;
        }
    }
    
    static final class BigDecimalAddReducer implements Reducer<BigDecimal>, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = -830758681673022439L;

        public BigDecimal op(BigDecimal a, BigDecimal b) {
            return a.add(b);
        }

        /** @return Preserves singleton property */
        private Object readResolve() {
            return ADD_REDUCER;
        }
    }
    
    
    static final class BigDecimalMultiplyReducer implements Reducer<BigDecimal>, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = -130758681673022439L;

        public BigDecimal op(BigDecimal a, BigDecimal b) {
            return a.multiply(b);
        }

        /** @return Preserves singleton property */
        private Object readResolve() {
            return MULTIPLY_REDUCER;
        }
    }
    
    static final class BigDecimalDivideReducer implements Reducer<BigDecimal>, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = -330758681673022439L;

        public BigDecimal op(BigDecimal a, BigDecimal b) {
            return a.divide(b);
        }

        /** @return Preserves singleton property */
        private Object readResolve() {
            return DIVIDE_REDUCER;
        }
    }
         static final class BigDecimalAddOp implements Op<BigDecimal, BigDecimal>, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = -6604604690824553900L;

        private final BigDecimal add;

        public BigDecimalAddOp(BigDecimal add) {
            this.add = add;
        }

        public BigDecimal op(BigDecimal a) {
            return a.add(add);
        }
    }
    static final class BigDecimalSubtractOp implements Op<BigDecimal, BigDecimal>, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = -23423423410L;

        private final BigDecimal subtract;

        public BigDecimalSubtractOp(BigDecimal subtract) {
            this.subtract = subtract;
        }

        public BigDecimal op(BigDecimal a) {
            return a.subtract(subtract);
        }
    }
    static final class BigDecimalDivideOp implements Op<BigDecimal, BigDecimal>, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = 661378303438906777L;

        private final BigDecimal divide;

        public BigDecimalDivideOp(BigDecimal divide) {
            this.divide = divide;
        }

        public BigDecimal op(BigDecimal a) {
            return a.divide(divide);
        }
    }

    static final class BigDecimalMultiplyOp implements Op<BigDecimal, BigDecimal>, Serializable {
        /** serialVersionUID. */
        private static final long serialVersionUID = 6099641660816235381L;

        private final BigDecimal multiply;

        public BigDecimalMultiplyOp(BigDecimal multiply) {
            this.multiply = multiply;
        }

        public BigDecimal op(BigDecimal a) {
            return a.multiply(multiply);
        }
    }}