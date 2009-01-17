package org.codehaus.cake.internal.cache.view;

public enum Operations {
    //Format
    //1 bigop - smallop
    // 012      023
    //Reducer - min
    // 012      024
    //Reducer - max
    
    
    // Reductions
    REDUCE_MAX_NATURAL,
    REDUCE_MIN_NATURAL,
    REDUCE_WITH_REDUCER_AND_NULL, //With 1 arguments
    REDUCE_WITH_REDUCRE_AND_INITIAL,
}
